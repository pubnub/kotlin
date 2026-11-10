---
name: sdk-review
description: PubNub Kotlin/Java SDK code review — applies the shared review rules from .claude/review-rules.md and the public-API discipline from CLAUDE.md. Asks whether to review the full branch vs master or only uncommitted changes, then produces a read-only report (no edits).
---

# sdk-review

A repo-specific, **read-only** code review for this dual-SDK codebase (Kotlin Multiplatform + Java/GSON, sharing `pubnub-kotlin-impl`). Rules come from `.claude/review-rules.md` (tone, path filters, path-specific rules) and `CLAUDE.md` ("Architecture" + "Public-API discipline"). Read `.claude/review-rules.md` before Step 2. Treat those two files as the source of truth — if they ever conflict with this skill, they win.

## Non-goals (hard constraints)

- Do NOT use `Edit`, `Write`, or `NotebookEdit`. This skill produces a report only.
- Do NOT run mutating Gradle tasks (`ktlintFormat`, `publishAllPublicationsToRepoRepository`, anything that writes outside `build/`). Recommending them in the report is fine; running them is not.
- Do NOT commit, stage, push, or modify git state.
- If the user wants fixes, they re-run after applying changes themselves, or use the general-purpose `code-review` skill.

## Step 1 — Ask scope

Before reading any code, call `AskUserQuestion` with exactly these two options:

- **Branch vs master** — review everything that has changed on the current branch since it diverged from `master`, including uncommitted changes.
- **Uncommitted only** — review only staged + unstaged + untracked changes in the working tree.

Do not assume a default. Wait for the answer.

## Step 2 — Collect the change set

Based on the answer, run via `Bash`:

**Branch vs master:**
```
git status
git diff master...HEAD --name-status
git diff master...HEAD
git diff HEAD            # uncommitted on top of branch
```

**Uncommitted only:**
```
git status
git diff HEAD            # staged + unstaged
# plus read each untracked file listed by git status
```

For every changed file in the resulting set:

1. Apply the **path filters** from `.claude/review-rules.md`.
2. Read the file fully with `Read`. Diff context alone is not enough — the path-specific rules need surrounding code (class signatures, default values, `when` exhaustiveness, etc.).

## Step 3 — Apply path-specific rules

Match each surviving file against the globs in the "Path-specific rules" section of `.claude/review-rules.md`. A file may match more than one rule — apply all that match.

## Step 4 — Repo-wide concerns

Apply these to every reviewed file regardless of path:

- **Correctness, performance, security** — the tone `.claude/review-rules.md` asks for. Be concise and actionable.
- **Public-API discipline** (the "Public-API discipline" section of `AGENTS.md`) — any `*-api` or `*-core-api` change is potentially customer-visible. Default-value changes and enum/sealed-class shape changes are behavior breaks; flag them even if compilation succeeds.
- **Dual-SDK propagation** — when `pubnub-kotlin-impl` is touched, the Java SDK is affected via delegation. Note this explicitly in the report.
- **KMP target hygiene** — `expect` requires `actual` on every enabled target. Watch for `commonMain` code that depends on JVM-only APIs.
- **No new comments unless they explain non-obvious "why"** — flag explanatory comments that just restate the code.

## Step 5 — Output the report

Produce a single Markdown report as your final message. **No code edits, no `Write` calls.** Structure:

```
## Summary
<one paragraph: scope (branch-vs-master | uncommitted), file count after filters, headline risks>

## Findings

### Blocker
- `path/to/File.kt:42` — <issue> — rule: <which rule from above, e.g. "core-api default-value change">

### Major
- ...

### Minor
- ...

### Nit
- ...

## Suggested verifications
- `./gradlew ktlintCheck` (always, if any Kotlin file changed)
- `./gradlew :pubnub-kotlin:pubnub-kotlin-impl:test` (if impl touched)
- `./gradlew :pubnub-kotlin:pubnub-kotlin-impl:integrationTest` (if impl touched)
- `./gradlew :pubnub-gson:pubnub-gson-impl:integrationTest` (if impl OR gson touched — both SDKs share internals)
- `./gradlew check` (broad safety net)
- `./gradlew publishAllPublicationsToRepoRepository -PRELEASE_SIGNING_ENABLED=false` (if build-logic or any *-api module touched)

## Out of scope / not reviewed
- <files skipped by path filters, with one-line reason each>
```

Severity guide:
- **Blocker** — public-API behavior break without restoration path; thread-safety bug; security issue; broken `expect`/`actual`; build/publication misconfiguration.
- **Major** — likely correctness or perf bug, leak, missing KDoc on a public default-value change.
- **Minor** — style, naming, redundant code, suboptimal but correct.
- **Nit** — cosmetic.

If the diff is empty after filtering, say so plainly and stop — do not invent findings.