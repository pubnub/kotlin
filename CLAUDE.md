# CLAUDE.md

@AGENTS.md

## Claude Code notes

- **Where guidance goes:** put guidance that applies to all agents in `AGENTS.md`. Keep only Claude Code-specific notes in this file.
- **Hooks and permissions** (`.claude/settings.json`): a `PreToolUse` hook (`.claude/hooks/block-raw-gradle.sh`) blocks raw `./gradlew` and points to `scripts/dev/`; `scripts/dev/check.sh` is pre-approved, the integration scripts are not. Personal overrides go in `.claude/settings.local.json` (git-ignored).
- **Skills** (`.claude/skills/`): `brief` (problem brief + decision-focused plan), `sdk-review` (read-only code review), `securityReview`, `attackSimulation`. All review skills read `.claude/review-rules.md`.