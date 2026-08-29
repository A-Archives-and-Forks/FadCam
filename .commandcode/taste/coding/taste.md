# Coding Preferences

- Never uses fake, hardcoded, or fallback data — including synthesized/fabricated sensor values or coords-only "manual" locations; show real data or an honest 0/error instead. Confidence: 0.98
- Does not leave TODOs; expects complete, working, single-solution implementations and removal of old junk. Confidence: 0.95
- Follows industry standards and best practices. Confidence: 0.85
- Avoids Supabase edge functions for live/hot-path logic to protect free-tier limits; prefers scalable non-edge-function architecture (e.g., the Redis/worker approach used for viewer tracking). Confidence: 0.9
- Treats security as non-negotiable; validates and verifies rather than assuming. Confidence: 0.85
- Maintains a zero-log privacy policy for cloud streaming: never process or store real user IP addresses (local-network private IPs are acceptable). Confidence: 0.9
- Formats timestamps consistently everywhere, matching the admin dashboard (formatted time with a tooltip showing the full timestamp). Confidence: 0.85
- Does not want sensitive information (secrets, tokens, credentials, machine-specific paths) committed to git; wants staged changes scanned and verified safe before committing. Confidence: 0.85
- Avoids hardcoding important/tunable values; keeps internal sensor/logic constants clearly separated from user-facing settings so they can't interfere. Confidence: 0.8
- Prefers efficient, low-battery/performance-impact implementations — real-time responsiveness without loading the phone. Confidence: 0.9
- Adds comprehensive diagnostic logging to make future debugging self-sufficient, but keeps it efficient and low-overhead (no log spam or per-frame logging). Confidence: 0.9
- Never logs sensitive values at full precision (GPS coordinates, PII); redacts them to coarse precision to avoid privacy leaks. Confidence: 0.95
- Wants the whole codebase swept for real IPs/PII — not just logs and test files, but also UI placeholder data, watermark preview cards (including their lat/long preview display), layout XML samples, and comments. Confidence: 0.9
- Does not want test-only hooks/constructors cluttering production code; keeps the production API surface clean and achieves testability via industry-standard frameworks (Robolectric, reflection) or extracting pure logic into testable classes. Confidence: 0.9
 Wants test files and related config cleaned up after a testing task so no old clutter accumulates — removes dead/skipped tests that silently pass (false confidence), now-unused test permissions (e.g., leftover ACCESS_MOCK_LOCATION), and unused imports; keeps the suite lean and honest. Prefers outright deletion of whole files over keeping cleaned/slimmed versions when a test is impractical to run reliably (e.g., instrumented tests needing a device + live GPS fix), especially when the logic is already covered by deterministic JVM tests. Confidence: 0.9
