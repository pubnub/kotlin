# AGENTS.md

Guidance for AI coding agents working in this repository, and the single source of truth for guidance shared by all agents. Agents that read `AGENTS.md` (Cursor, Codex and others) load it directly; `CLAUDE.md` imports it for Claude Code. Put guidance that applies to every agent here, and keep only Claude Code-specific notes in `CLAUDE.md`.

Shared review rules (tone, path filters, path-specific rules) live in `.claude/review-rules.md`.

## Build & test

Java 21 is required to drive the build (CI uses Zulu 21). Produced libraries target JVM 8 via `jvmToolchain(8)`.

Common Gradle tasks:

- `./gradlew check` — unit tests plus static analysis (ktlint, detekt, Checkstyle, SpotBugs) across all modules; does **not** include `integrationTest`. CI splits it: `run-tests.yml` runs `check` with the analysis tasks excluded (`-x`), `run-validations.yml` runs them separately
- `./gradlew test` — JVM unit tests
- `./gradlew integrationTest` — runs integration tests in **both** SDKs against real PubNub (see "Test credentials" below). The Kotlin and Java SDKs maintain **separate, parallel** integration test suites:
  - Kotlin: `pubnub-kotlin/pubnub-kotlin-impl/src/integrationTest/kotlin/` → `./gradlew :pubnub-kotlin:pubnub-kotlin-impl:integrationTest`
  - Java (GSON): `pubnub-gson/pubnub-gson-impl/src/integrationTest/java/` → `./gradlew :pubnub-gson:pubnub-gson-impl:integrationTest`
  - When changing shared internals in `pubnub-kotlin-impl`, run both — the Java suite exercises the same code through the GSON delegation layer.
- `./gradlew ktlintFormat` / `./gradlew ktlintCheck` — apply / verify Kotlin formatting
- `./gradlew publishAllPublicationsToRepoRepository -PRELEASE_SIGNING_ENABLED=false` — local publish; same task CI uses to validate the build is releasable
- `./gradlew :module-path:test --tests "com.pubnub.foo.BarTest"` — run a single test class
- `./gradlew :pubnub-kotlin:pubnub-kotlin-impl:test --tests "com.pubnub.foo.BarTest.specificMethod"` — run one test method

### Acceptance / Cucumber tests

These run from `pubnub-kotlin-impl` and consume external feature files from a `sdk-specifications` repo:

```bash
./gradlew cucumber --tests RunMainCucumberTest -Dcucumber.features="../../sdk-specifications/features/"
# Beta suite (allowed to fail in CI):
./gradlew cucumber --tests RunBetaCucumberTest -Dcucumber.features="../../sdk-specifications/features/"
```

The CI workflow symlinks `sdk-specifications` into `pubnub-kotlin/pubnub-kotlin-impl/src/test/resources/`. Locally, point `-Dcucumber.features=` at your checkout.

### Test credentials

`test-example.properties` documents the required keys. To run integration tests locally, copy it to `test.properties` and fill in real values, or export env vars: `PUB_KEY`, `SUB_KEY`, `PAM_PUB_KEY`, `PAM_SUB_KEY`, `PAM_SEC_KEY`. CI uses `SDK_*`-prefixed env vars.

### Multiplatform targets

KMP targets are gated by `gradle.properties` flags (`ENABLE_TARGET_JS`, `ENABLE_TARGET_IOS_OTHER`, `ENABLE_TARGET_IOS_SIMULATOR_ARM64`, `ENABLE_TARGET_IOS_ALL`). The default profile in this repo enables JVM + JS + iOS simulator (arm64). The iOS targets pull `PubNubSwift` via CocoaPods (`pod repo update` may be needed before a clean build). To build against a local Swift SDK checkout, clone it outside this repo and set `SWIFT_PATH` in `gradle.properties` to that path instead of the published version. There is no Swift checkout inside this repo; do not treat any local Swift clone as an up-to-date reference for Swift SDK behavior unless the user points to it.

## Working rules

- **Behavior fixes ship a regression test.** After changing production behavior, extend or add a test that fails without the fix. Do not stop at the code edit. For bugs, write the failing test first.
- **Source edits go through the `Edit` tool** so each diff can be reviewed. Do not script source changes through Bash (`sed`, `perl`, heredocs, etc.).
- **Do not start implementing until the user explicitly says to.** Approving a plan is not a go-ahead.
- **Git:** commit or push only when asked. Stage new files explicitly with `git add` before committing; `git commit -am` silently skips untracked files.
- **Builds, tests and linters go through `scripts/dev/`, never raw `./gradlew`** (in Claude Code a `PreToolUse` hook in `.claude/settings.json` blocks it). The wrappers print one `PASS`/`FAIL` line plus the first failing tests/errors and write the full output to `build/agent-logs/`; read that log only if the summary is not enough.
  - `scripts/dev/check.sh lint | static | compile | unit [module] [--tests X]` — `lint`, `static`, `compile` can be combined in one run (`check.sh static compile`). `static` = ktlint + detekt + Checkstyle + SpotBugs, same as CI.
  - Before reporting Kotlin changes as done, run `scripts/dev/check.sh lint`.
  - Integration tests are **opt-in** (real PubNub, need credentials, see "Test credentials"): `scripts/dev/integration-kotlin.sh`, `integration-gson.sh`, or `integration-both.sh`. Run them only when the user asks; when shared `pubnub-kotlin-impl` internals changed, recommend both suites.
  - `check.sh format` rewrites files; run it only on request.

## Architecture

The repo ships two parallel SDK frontends — Kotlin (multiplatform) and Java (GSON) — backed by a shared JVM implementation.

### Module layout (under `pubnub-kotlin/`)

- `pubnub-kotlin-core-api` — KMP source-only module. Behavior-shaping abstractions: `PNConfiguration`, `RetryConfiguration`, `RetryableEndpointGroup`, `PNLogVerbosity`, `PNHeartbeatNotificationOptions`, logging config. **Default values and enum/sealed-class entries here are part of the public contract.** `commonMain` is platform-agnostic; `jvmMain` adds Retrofit/OkHttp/Gson; `jsMain` wraps the npm `pubnub` package; `appleMain` / `nonJvm` / `nonJs` are intermediate source sets.
- `pubnub-kotlin-api` — KMP public API surface: endpoint contracts (under `com.pubnub.api.endpoints.*`), result models (`com.pubnub.api.models.consumer.*`), `v2` subscriptions/entities/callbacks. Depends on `core-api`.
- `pubnub-kotlin-impl` — **JVM-only** concrete implementation. All `com.pubnub.internal.*` packages live here: `subscribe` (event engine), `presence`, `eventengine`, `endpoints`, `managers`, `interceptor`, `retry`, `logging`, `crypto`, `services` (Retrofit interfaces), `workers`. Has a `generateVersion` task that emits a `Version.kt` from `VERSION_NAME` in `gradle.properties`.
- `pubnub-kotlin-coroutines` — coroutine extensions over the API.
- `pubnub-kotlin-test` — KMP test helpers used by all `commonTest`/`jvmTest` source sets.
- `pubnub-kotlin-docs` — Dokka aggregation.

### Java / GSON SDK (under `pubnub-gson/`)

- `pubnub-gson-api` — Java public API (Java 8+ compatible, no Kotlin types leaked).
- `pubnub-gson-impl` — Java implementation that **delegates to `pubnub-kotlin-impl`**, so a bug fix in the internals usually flows to both SDKs without code change. Uses Lombok and integration tests in `src/integrationTest/java/`.

### Other

- `migration_utils/` — included build (`includeBuild`) holding migration tooling, not published.
- `examples/kotlin-app`, `examples/java-app` — runnable sample apps.

### Build infrastructure

`build-logic/gradle-plugins` is an `includeBuild` providing the `pubnub.*` plugin IDs that all modules apply:

| Plugin | Effect |
|---|---|
| `pubnub.shared` | Maven publishing (skipped for `*-test` modules), ktlint, custom editorconfig, applies the `ktlint-custom-rules` ruleset |
| `pubnub.kotlin-library` | JVM Kotlin library, toolchain 8, `-Xjvm-default=all`, `javaParameters=true` |
| `pubnub.java-library` | Plain Java library |
| `pubnub.multiplatform` | KMP setup with JVM + (optional) JS + (optional) iOS targets, CocoaPods integration for `PubNubSwift`, default hierarchy with `nonJvm` / `nonJs` intermediate source sets |
| `pubnub.test` | Adds `failFast`, JUnit Platform, excludes `**/contract/*.class` from `test`, applies Jacoco |
| `pubnub.integration-test` | Adds `integrationTest` source set + task; `integrationTestImplementation` extends both `implementation` and `testImplementation` |
| `pubnub.ios-simulator-test` | iOS simulator test wiring |
| `pubnub.dokka` | KDoc generation |

Custom ktlint rules live in `build-logic/ktlint-custom-rules` (also an `includeBuild`).

### Public-API discipline

The `*-api` and `*-core-api` modules are consumed by external customers. When changing them:

- Default values on public properties / Builder setters / companion-object constants (timeouts, retry bounds, concurrency caps) are part of the behavior contract — flag changes in PRs.
- Adding/removing/reordering entries in public enums or sealed-class hierarchies is a source-compat break for users with exhaustive `when`.
- KMP `expect` declarations require matching `actual` on every enabled target.