# Review rules

Shared, canonical review rules for this repo. Consumed by the `sdk-review`, `securityReview`, and
`attackSimulation` skills. `CLAUDE.md` ("Architecture" + "Public-API discipline") remains the source of
truth for architecture — if it ever conflicts with this file, it wins.

## Tone

Be concise and actionable. Focus on correctness, performance, and security issues.

## Path filters

Skip files matching:

- `node_modules/**`
- `build/**`
- `**/build/**`
- `**/*.lock`
- `kotlin-js-store/**`
- `**/sdk-specifications/**`
- `**/.gradle/**`
- `**/generated/**`
- `swift/**`
- `migration_utils/**`

## Path-specific rules

Match each file against the globs below. A file may match more than one rule — apply all that match.

### `pubnub-kotlin/pubnub-kotlin-api/**` — public KMP API

- Backward compat of method signatures, data class fields, and `Builder` shapes.
- Changes to default parameter values are public-API changes even when signatures compile.
- KDoc must reflect new/changed behavior in the same diff.
- Every `expect` declaration has matching `actual` on every enabled target (JVM + JS + iOS simulator arm64 by default; see `gradle.properties` `ENABLE_TARGET_*`).

### `pubnub-kotlin/pubnub-kotlin-impl/**` — shared JVM implementation

- Thread safety on shared mutable state (managers, registries, subscription/listener maps).
- Error handling and resource cleanup (close `Call`/`Response`, cancel coroutines, dispose listeners).
- No subscription/listener leaks — anything registered must have a deregistration path.
- Efficient collections / no accidental O(n²) on hot paths.
- **Dual-SDK reminder:** changes here also affect the Java/GSON SDK via delegation. Recommend running **both** integration suites in the report.

### `pubnub-kotlin/pubnub-kotlin-core-api/**` — behavior-shaping surface

This is the highest-risk module. Flag as **Major** or **Blocker**:

- Any change to a default value on a public property, `Builder` setter, or companion-object constant (timeouts, retry bounds, concurrency caps, `MIN_DELAY` / `MAX_DELAY` / `MAX_RETRIES`, etc.) — even if the signature is unchanged. Require: release-note entry + a documented restoration path ("set `X = oldValue` to restore prior behavior").
- Any addition, removal, rename, or reordering of entries in public enums (`RetryableEndpointGroup`, `PNLogVerbosity`, `PNHeartbeatNotificationOptions`) or public sealed-class hierarchies (`RetryConfiguration` variants) — these break exhaustive `when` for downstream users.
- KDoc must be updated to match any default-value change in the same PR.
- Platform-agnostic — no JVM-specific types should leak into common code.

### `pubnub-gson/pubnub-gson-api/**` — Java public API

- Java 8+ source compatibility (no `var` in non-local positions, no Kotlin-only types in public signatures).
- No Kotlin types leaked (no `Result<T>`, no `Pair`, no `Lazy`, no nullable Kotlin generics with `?`).

### `pubnub-gson/pubnub-gson-impl/**` — Java implementation

- Concurrency correctness for callback-style APIs.
- Correct delegation into `pubnub-kotlin-impl` (don't reimplement logic that already lives there).
- Listener/callback leak audit — anything registered must be releasable.

### `build-logic/**` — Gradle plugins and build configuration

- Publication / signing configuration correctness.
- Version-catalog (`libs.versions.toml`) references are valid.
- Plugin IDs (`pubnub.shared`, `pubnub.kotlin-library`, `pubnub.multiplatform`, `pubnub.test`, `pubnub.integration-test`, `pubnub.dokka`, etc.) are applied consistently.

### `**/*Test*` — test code (any SDK)

- Assertions are meaningful, not trivially true.
- No flaky timing/ordering dependencies (sleeps, hardcoded waits, reliance on iteration order).
- Proper isolation — tests don't bleed state into each other.