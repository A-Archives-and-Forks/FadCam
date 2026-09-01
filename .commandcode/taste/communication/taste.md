# Communication Preferences

- Prefers "caveman skill" in ultra mode for ultra-concise, token-saving responses in chat. Confidence: 0.95
- Wants technical explanations to be concise and use real-world analogies, progressing from ELI5 up to expert level so concepts are learned permanently. Confidence: 0.85
- Prefers to always be very concise in responses. Confidence: 0.95
- Prefers honest, non-overpromising answers — no false "always works, zero bugs" guarantees; values verified, evidence-backed claims. Confidence: 0.85
- Applies the privacy/redaction policy to test code too — no real-world PII (real GPS coordinates from user logs, IPs, credentials) in test files; uses clearly synthetic values instead. Confidence: 0.9
- Wants to see actual test results/output as proof when tests pass or a fix works — so they can verify what manual testing would look like — not just summary claims of success. Confidence: 0.8
- Wants commit messages to quantify the scope of an upgrade/cleanup (e.g., explicitly asked "how many deprecated apis did we upgrade" — expects counts of APIs modernized, files changed, insertions/deletions) so the commit itself documents its impact. Confidence: 0.85
