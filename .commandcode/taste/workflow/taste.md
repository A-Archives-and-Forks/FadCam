# Workflow Preferences

- Never scp/rsync files; does all work locally and the user pushes to GitHub, then the deploy script handles the rest (the env-file scp is an exception that should remain). Confidence: 0.9
- Never edits the nested fadcam repo; always edits the main FadCam repo, and the user handles pull/push. Confidence: 0.9
- Asks before making structural changes instead of silently adding templates or TODOs. Confidence: 0.8
- Documents system architecture and usage semantics in markdown files for later reference and Q&A, based on verified facts rather than assumptions. Confidence: 0.8
- Reviews staged git changes before committing and wants guidance on whether files should be committed or gitignored rather than committing blindly. Confidence: 0.8
- Prefers deleting useless/junk files that are not related to the app or context, keeping the repo focused on relevant content only. Confidence: 0.85
- Considers `git commit` (and similar git operations) a "dangerous command" and avoids running it themselves; prefers the agent to execute commits and other risky git steps on request. Confidence: 0.8
