# Workflow Preferences

- Never scp/rsync files; does all work locally and the user pushes to GitHub, then the deploy script handles the rest (the env-file scp is an exception that should remain). Confidence: 0.9
- Never edits the nested fadcam repo; always edits the main FadCam repo, and the user handles pull/push. Confidence: 0.9
- Asks before making structural changes instead of silently adding templates or TODOs. Confidence: 0.8
- Documents system architecture and usage semantics in markdown files for later reference and Q&A, based on verified facts rather than assumptions. Confidence: 0.8
- Reviews staged git changes before committing and wants guidance on whether files should be committed or gitignored rather than committing blindly. Confidence: 0.9
- Prefers deleting useless/junk files that are not related to the app or context, keeping the repo focused on relevant content only. Confidence: 0.9
- Considers `git commit` (and similar git operations) a "dangerous command" and avoids running it themselves; prefers the agent to execute commits and other risky git steps on request. Confidence: 0.8
- Never adds `Co-authored-by` or other AI attribution trailers to git commit messages. Confidence: 0.95
- Prefers deep, exhaustive root-cause analysis over quick fixes: dig into the source, enumerate all possible root causes and edge cases (crash, kill, stop, restart, permission revoke, etc.), and produce a comprehensive todo list so the result is robust rather than buggy. Confidence: 0.9
- Wants log/source files read fully to the end before diagnosing, not skimmed. Confidence: 0.85
- When given a user-reported bug or clue, verifies it against the actual code first — is it real, and did prior fixes already cover it — and traces the exact scenario end-to-end before declaring it solved or not. Confidence: 0.85
