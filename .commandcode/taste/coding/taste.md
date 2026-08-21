# Coding Preferences

- Never uses fake, hardcoded, or fallback data; show real data or surface a clear error instead. Confidence: 0.95
- Does not leave TODOs; expects complete, working, single-solution implementations and removal of old junk. Confidence: 0.9
- Follows industry standards and best practices. Confidence: 0.85
- Avoids Supabase edge functions for live/hot-path logic to protect free-tier limits; prefers scalable non-edge-function architecture (e.g., the Redis/worker approach used for viewer tracking). Confidence: 0.9
- Treats security as non-negotiable; validates and verifies rather than assuming. Confidence: 0.85
- Maintains a zero-log privacy policy for cloud streaming: never process or store real user IP addresses (local-network private IPs are acceptable). Confidence: 0.9
- Formats timestamps consistently everywhere, matching the admin dashboard (formatted time with a tooltip showing the full timestamp). Confidence: 0.85
- Does not want sensitive information (secrets, tokens, credentials, machine-specific paths) committed to git; wants staged changes scanned and verified safe before committing. Confidence: 0.85
