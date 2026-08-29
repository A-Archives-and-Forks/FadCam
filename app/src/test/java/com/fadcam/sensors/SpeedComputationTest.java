package com.fadcam.sensors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.location.Location;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * JVM unit tests for the GPS speed computation in {@link SensorDataProvider},
 * run under Robolectric so real {@link Location} objects and real geo math
 * (distanceTo) are available without a device.
 *
 * Covers the decision logic: direct Doppler speed, distance/time fallback,
 * keep-last-known on duplicate fixes, honest 0 when stationary or stale.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class SpeedComputationTest {

    private SensorDataProvider provider;

    // Robolectric freezes SystemClock.elapsedRealtimeNanos(), so we drive the
    // location timestamps with our own monotonic counter — exactly what a real
    // GPS chipset delivers (fixes ~1s apart).
    private long elapsedNanos = 1_000_000_000L;

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.getApplication();
        provider = SensorDataProvider.getInstance(context);
        elapsedNanos = 1_000_000_000L;
    }

    @After
    public void tearDown() {
        SensorDataProvider.resetInstance();
    }

    /** Build a real Location (Robolectric) at the given fix, ~1s later each call. */
    private Location gpsFix(double lat, double lon, float accuracy) {
        Location loc = new Location("gps");
        loc.setLatitude(lat);
        loc.setLongitude(lon);
        loc.setAccuracy(accuracy);
        loc.setTime(System.currentTimeMillis());
        loc.setElapsedRealtimeNanos(elapsedNanos += 1_000_000_000L);
        return loc;
    }

    private Location gpsFixWithSpeed(double lat, double lon, float accuracy, float speedMs) {
        Location loc = gpsFix(lat, lon, accuracy);
        loc.setSpeed(speedMs);
        return loc;
    }

    @Test
    public void directSpeedWinsWhenProvided() {
        // 36 km/h = 10 m/s
        provider.updateLocation(gpsFixWithSpeed(1.234567, -1.234567, 5f, 10f));
        assertEquals(36.0, provider.getSpeedKmh(), 0.01);
    }

    @Test
    public void computedFallbackUsedWhenNoSpeedField() {
        // Fix 1 at a known (synthetic) point.
        provider.updateLocation(gpsFix(1.234500, -1.234500, 5f));

        // Fix 2 ~10m north (0.00009 deg lat), 1s later (controlled clock).
        provider.updateLocation(gpsFix(1.234590, -1.234500, 5f));

        // Real distanceTo(10m) / 1s = ~10 m/s = 36 km/h.
        double speed = provider.getSpeedKmh();
        assertTrue("expected ~36 km/h but got " + speed, speed > 20 && speed < 55);
    }

    @Test
    public void keepsLastKnownSpeedWhenSameFixReFed() {
        Location moving = gpsFixWithSpeed(1.234567, -1.234567, 5f, 10f); // 36 km/h
        provider.updateLocation(moving);
        assertEquals(36.0, provider.getSpeedKmh(), 0.01);

        // Re-feeding the SAME fix object (same elapsed timestamp) must not
        // clobber the running value.
        provider.updateLocation(moving);
        assertEquals(36.0, provider.getSpeedKmh(), 0.01);
    }

    @Test
    public void zeroSpeedIsReportedWhenFixSaysStationary() {
        // Real "stopped" reading: hasSpeed=true, speed=0 -> 0.
        provider.updateLocation(gpsFixWithSpeed(1.234567, -1.234567, 5f, 0f));
        assertEquals(0.0, provider.getSpeedKmh(), 0.01);
    }

    @Test
    public void staleLocationReportsZero() {
        provider.updateLocation(gpsFixWithSpeed(1.234567, -1.234567, 5f, 10f));
        assertEquals(36.0, provider.getSpeedKmh(), 0.01);

        // Age the last-fix timestamp past the 10s staleness window. We use
        // reflection since there is no production hook (tests must not drive
        // production API surface).
        try {
            java.lang.reflect.Field f = SensorDataProvider.class.getDeclaredField("lastLocationUpdateTime");
            f.setAccessible(true);
            f.setLong(provider, System.currentTimeMillis() - 15_000L);
        } catch (Exception e) {
            throw new AssertionError("Cannot age last-fix timestamp", e);
        }

        assertEquals(0.0, provider.getSpeedKmh(), 0.01);
    }

    @Test
    public void speedZeroWhenNoFixEverArrived() {
        assertEquals(0.0, provider.getSpeedKmh(), 0.01);
    }

    @Test
    public void directSpeedIsAccuracyIndependent() {
        // Doppler speed stays valid even with a poor position accuracy fix.
        provider.updateLocation(gpsFixWithSpeed(1.234567, -1.234567, 200f, 10f));
        assertEquals(36.0, provider.getSpeedKmh(), 0.01);
    }

    @Test
    public void computedFallbackRejectsInaccurateFixes() {
        // A coarse fix (200m) next to a precise one must NOT produce a speed.
        provider.updateLocation(gpsFix(1.234500, -1.234500, 200f));
        provider.updateLocation(gpsFix(1.234590, -1.234500, 5f));

        // distance fallback rejected due to coarse previous fix -> 0.
        assertEquals(0.0, provider.getSpeedKmh(), 0.01);
    }
}
