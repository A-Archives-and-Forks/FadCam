package com.fadcam.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fadcam.SharedPreferencesManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Tests for the location-feed gating decision. This is the fix for the bug
 * where speed/altitude watermarks stayed 0 when the "location watermark"
 * toggle was off: the GPS feed must be driven by ANY location-dependent
 * feature, not just the coordinates toggle.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class LocationFeedGateTest {

    private SharedPreferencesManager prefs(boolean loc, boolean embed, boolean utm,
                                            boolean speed, boolean alt, boolean acc,
                                            boolean compass, boolean weather) {
        SharedPreferencesManager p = mock(SharedPreferencesManager.class);
        when(p.isLocalisationEnabled()).thenReturn(loc);
        when(p.isLocationEmbeddingEnabled()).thenReturn(embed);
        when(p.isUtmEnabled()).thenReturn(utm);
        when(p.isSpeedEnabled()).thenReturn(speed);
        when(p.isAltitudeEnabled()).thenReturn(alt);
        when(p.isAccuracyEnabled()).thenReturn(acc);
        when(p.isCompassEnabled()).thenReturn(compass);
        when(p.isWeatherEnabled()).thenReturn(weather);
        return p;
    }

    @Test
    public void speedOnlyWithoutLocationWatermarkStillNeedsFeed() {
        // The exact user scenario: speed ON, coordinates watermark OFF.
        SharedPreferencesManager p = prefs(false, false, false, true, false, false, false, false);
        assertTrue(LocationFeedGate.needsLocationData(p));
    }

    @Test
    public void altitudeOnlyWithoutLocationWatermarkStillNeedsFeed() {
        SharedPreferencesManager p = prefs(false, false, false, false, true, false, false, false);
        assertTrue(LocationFeedGate.needsLocationData(p));
    }

    @Test
    public void accuracyOnlyWithoutLocationWatermarkStillNeedsFeed() {
        SharedPreferencesManager p = prefs(false, false, false, false, false, true, false, false);
        assertTrue(LocationFeedGate.needsLocationData(p));
    }

    @Test
    public void compassOnlyWithoutLocationWatermarkStillNeedsFeed() {
        SharedPreferencesManager p = prefs(false, false, false, false, false, false, true, false);
        assertTrue(LocationFeedGate.needsLocationData(p));
    }

    @Test
    public void weatherOnlyWithoutLocationWatermarkStillNeedsFeed() {
        SharedPreferencesManager p = prefs(false, false, false, false, false, false, false, true);
        assertTrue(LocationFeedGate.needsLocationData(p));
    }

    @Test
    public void locationWatermarkAloneNeedsFeed() {
        SharedPreferencesManager p = prefs(true, false, false, false, false, false, false, false);
        assertTrue(LocationFeedGate.needsLocationData(p));
    }

    @Test
    public void embeddingAloneNeedsFeed() {
        SharedPreferencesManager p = prefs(false, true, false, false, false, false, false, false);
        assertTrue(LocationFeedGate.needsLocationData(p));
    }

    @Test
    public void utmAloneNeedsFeed() {
        SharedPreferencesManager p = prefs(false, false, true, false, false, false, false, false);
        assertTrue(LocationFeedGate.needsLocationData(p));
    }

    @Test
    public void nothingEnabledNeedsNoFeed() {
        SharedPreferencesManager p = prefs(false, false, false, false, false, false, false, false);
        assertFalse(LocationFeedGate.needsLocationData(p));
    }

    @Test
    public void nullPrefsNeedsNoFeed() {
        assertFalse(LocationFeedGate.needsLocationData(null));
    }
}
