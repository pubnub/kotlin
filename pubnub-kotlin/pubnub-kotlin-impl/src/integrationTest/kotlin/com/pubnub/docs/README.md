# Live docs snippet tests

The tests in `com.pubnub.docs` run the code samples of the Kotlin and Java documentation against real PubNub keysets. The test files are the future docs files: a region sits in the test body exactly as the docs show it, and the assertions follow the end marker. The tier lives in the existing `integrationTest` source sets of both SDK modules:

- Kotlin: `pubnub-kotlin/pubnub-kotlin-impl/src/integrationTest/kotlin/com/pubnub/docs/` (this folder, it also holds the shared support code)
- Java: `pubnub-gson/pubnub-gson-impl/src/integrationTest/java/com/pubnub/docs/` (uses the Kotlin support through the `integrationTestSupport` configuration of `pubnub-kotlin-impl`)

The existing `integrationTest` task does not run these tests (it excludes `com/pubnub/docs/**`). A separate task, `docsSnippetsTest`, runs only them. This README describes the support layer (step A). The test files of the pages are added in the next steps.

## How to run

Everything goes through one script. It checks the docs env names only, never prints a key value, and writes the full Gradle output to `build/agent-logs/`.

| Command | What it does | Needs keys |
| :-- | :-- | :-- |
| `scripts/dev/docs-snippets.sh lint [--tests X]` | Compiles both `integrationTest` source sets and runs the offline tools (`offline/`). With `--tests X` only the offline tests that match X run, for a single class | No |
| `scripts/dev/docs-snippets.sh run [group] [--tests X]` | Runs the live tests of one group, or all groups, in both SDK modules, one module after the other | Yes |
| `scripts/dev/docs-snippets.sh janitor` | Cleans the listed names, revokes the application-level Access Manager grant, waits until it is closed | Yes |
| `scripts/dev/docs-snippets.sh preflight` | Reads the keyset features through the Admin API and probes the open keyset | Yes |
| `scripts/dev/docs-snippets.sh docs-check [docs checkout]` | Classifies every docs `EmbeddedCode` tag, report only | No |

`LIVE_MANIFEST_WRITE=1 scripts/dev/docs-snippets.sh lint` regenerates `coverage.json`. `LIVE_EQUIVALENCE_STRICT=1` makes every golden that is not ported yet fail the offline run (used at the end of the port).

Do not run `scripts/dev/check.sh format` on files with markers. It can change the published text of a region.

## Keysets and env names

Three dedicated keysets, never the demo keys. There is no fallback: a missing role fails the run and names the missing env variables. Real environment variables win over the env file. The file is `LIVE_ENV_FILE`, or `.env.local` in this folder. Both are git-ignored. Copy `.env.example` and fill it in.

| Role | Env names | Features |
| :-- | :-- | :-- |
| `open` | `PUBLISH_KEY`, `SUBSCRIBE_KEY` | No Access Manager. Message Persistence with Delete-From-History, Presence with a rule on `*`, App Context with events, Files, Stream Controller with wildcard subscribe |
| `pam` | `PAM_PUBLISH_KEY`, `PAM_SUBSCRIBE_KEY`, `PAM_SECRET_KEY` | Access Manager with token revocation, plus the same Persistence, Presence, App Context, Files and Stream Controller settings |
| `datasync` | `DS_PUBLISH_KEY`, `DS_SUBSCRIBE_KEY`, `DS_SECRET_KEY` | DataSync, Access Manager with token revocation, Persistence, Presence, Stream Controller |
| `admin` | `PUBNUB_ADMIN_API_KEY` | Optional Admin API key for the keyset settings check of the preflight (read only). No test uses it, and CI does not set it |

Not secret: `DOCS_SNIPPETS_OPEN_KEYSET_ID`, `DOCS_SNIPPETS_PAM_KEYSET_ID`, `DOCS_SNIPPETS_DS_KEYSET_ID` are the keyset ids for the preflight. Switches: `LIVE_JANITOR=off` and `LIVE_PREFLIGHT=off` skip the cleanup and the feature check for single page runs, `LIVE_GROUP=<name>` selects a group.

The suite prints only `set` or `missing: NAME` per role. `Redact` masks every registered key, key shapes (`pub-c-...`, `sub-c-...`, `sec-c-...`), Access Manager tokens and the `auth`, `signature` and `token` query parameters in any message the suite prints. Assertion messages never carry a key value.

## Groups

`groups.json` is data only. One group is one `LIVE_GROUP=<name>` run and, later, one job of the Live tests workflow. The Gradle task reads the group and runs only its classes in both modules.

| Group | Roles | Pages |
| :-- | :-- | :-- |
| `messaging` | open | publish and subscribe, presence, channel groups, storage and playback, message actions, files, mobile push, the channel and channel group entities |
| `getting-started` | open, pam | configuration, logging, encryption, miscellaneous |
| `access-manager` | open, pam, datasync | Access Manager, Access Manager v2, including the DataSync grant samples |
| `app-context` | open | objects, the user and channel metadata entities |
| `datasync` | datasync | DataSync and its entities |

Kotlin and Java tests of the same page share literal names and keysets, so they are in the same group. `DocsToolsTest` fails when a docs test class is in no group or in two, when a listed class does not exist, or when a class uses a role that its group does not list.

## Rules

- A sample runs exactly once, as written, never retried and never inside a wait. Setup and cleanup outside the markers may use `Retry.retryTransient`.
- The text between `// snippet.<id>` and `// snippet.end` is published documentation. Never change it without the docs owner. The marker must stand alone on its line, with the space after the slashes. The three legacy files that keep `//snippet.` without the space are only valid because their docs tags pass `prefixes={['//']}`.
- Assertions come after `// snippet.end`, in the same test method. No test-only code in the visible text.
- `// snippet.hide` and `// snippet.show` only hide a line readers must not see, at most 3 lines. The hidden line of a "Main" sample is `com.pubnub.docs.support.LiveKeys.apply(this, "open")` inside the config builder, so the visible text keeps its `"demo"` keys.
- Literal channel, group and user ids stay as the docs show them. Every one must be listed in `support/resources/<Area>.kt`, and the lint fails on a literal that is not. The janitor cleans exactly those names.
- Ids are unique per docset and stable, they are part of a docs URL. A region copied from an old docs module file may carry `// origin: <old path>#<id>` on the line above its start marker when the id alone is ambiguous.
- No `@Disabled`, `@Ignore` or `Assumptions`. A sample that cannot run live is labelled in `snippet-exceptions.json` (with a reason) or marked `@CompileOnly("reason")`, never skipped silently.
- Check the SDK source for behavior, do not assume it.

## Helpers (`support/`)

| File | Purpose |
| :-- | :-- |
| `Env.kt` | Roles and env names, env file, never a fallback to demo keys |
| `LiveKeys.kt` | `LiveKeys.apply(builder, role)` for the hidden line of a "Main" sample, typed for the Kotlin builder, reflection for the Java builder |
| `LiveClients.kt` | Kotlin and Java clients per role and user, tracked, stopped after each test with listeners removed first |
| `Recorder.kt` | A `java.lang.reflect.Proxy` over the client. Records every `async` result (before the sample's own callback runs), every `sync` result and exception, and every `PNStatus` of a wrapped status listener. Swaps registered placeholder strings for live values at the call boundary. A sample that swallows errors in an empty failure block can no longer pass a failing call |
| `Wait.kt` | `eventually`, `settled` (3 passes 750 ms apart, 15 s), `expectEvent`, `connected`, `sleepToProve`. Timeout messages say what was seen |
| `Retry.kt` | `retryTransient` and `isTransient` (HTTP 5xx and 429, timeouts, network errors). Never a 4xx, never an assertion |
| `Cleanup.kt` | `cleanupAfterTest` (newest first) and `record(kind, name)` for resources a test creates |
| `LiveExtension.kt` | JUnit 5 extension, `@ExtendWith(LiveExtension::class)` on every live class. Resolves `LiveClients` and `Recorder` parameters |
| `Resources.kt`, `resources/` | The literal names per area, data only |
| `Janitor.kt`, `JanitorDataSync.kt` | See below |
| `Preflight.kt` | See below |
| `Redact.kt` | Masks keys and tokens in every printed message |

## Janitor

The janitor cleans exactly the names in `Resources`: history and files of listed channels, channel groups, App Context users, channels and the memberships between two listed sides, push registrations of listed device tokens, presence occupants that are listed user ids, and the listed DataSync objects (never a DataSync class). It never lists a keyset to delete what it finds, never matches by pattern and never changes keyset configuration. It revokes the application-level Access Manager grant on the Access Manager keysets. Every step is idempotent, a second pass removes nothing.

It runs before and after a live run, and alone with `scripts/dev/docs-snippets.sh janitor`. A failure before the run stops it. After the run only a failed grant revoke fails the run. Before a run with an Access Manager keyset, the extension waits until a client without a token is refused again, three times in a row.

## Preflight

The preflight fails in seconds with the name of every missing keyset feature. With `PUBNUB_ADMIN_API_KEY` and a keyset id per role it reads `GET /v2/keysets/{id}` and `/v2/keysets/{id}/config` from the Admin API (header `Authorization` with the raw key, `PubNub-Version: 2026-11-23`), compares the keyset's subscribe key with the loaded role (a yes or no, never the value) and lists each missing feature by name. Live probes on the open keyset always run: publish, Message Persistence, Delete-From-History, and Presence when the Admin API did not vouch for it. Without the admin key or the id the Admin API part of that role is skipped and a note says so.

## Offline checks

`offline/DocsToolsTest.kt` needs no keys and no network. Run it with `scripts/dev/docs-snippets.sh lint`.

- Marker lint (`support/tools/LintMarkers.kt`). Marker rules M1 to M7 and honesty rules H1 to H5, plus R7 for literal names. Its self test (`LintSelfTest.kt`) writes broken files to a temp directory and checks that each defect is reported and a clean file passes.
- Extractor proof. `Extractor.kt` is an exact port of the docs site's `extractSnippets` function, including the `prefixes` option. `extractor-fixtures.json` holds the TypeScript outputs for a fixture table and `docs-goldens.json` holds the TypeScript output for every embedded and orphan id of the old docs modules. The test runs the port on the fixtures and on the old files (read from the commit named in `DocsToolsTest` with `git show`, so the history must be fetched) and requires equal output. The JSON files are data only: the TypeScript output was captured once and is not regenerated by the tests.
- Manifest. `coverage.json` is generated from the markers, one entry per snippet id per file, plus the `snippet-map` from each old `path#id` to the new `file#id`. The test fails when it is stale.
- Groups, exceptions file shape, equivalence (each golden against the region now found in the new files, "not yet ported" is reported and does not fail), docs cross-check (`DocsCheck.kt`, classifies every docs tag as live, compile-only, exception or NOT COVERED), and the support helpers that need no keyset (recorder, waits, retry, redaction).

## Writing a page test

The pilot (`publishAndSubscribe/PublishAndSubscribeDocsTest.kt` and `.java`, 99 samples) is the model. Copy its shape. One test method per snippet id, arrangement above the region, the sample as the docs show it, assertions below `// snippet.end`.

### Layout of a test file

- `@file:Suppress("ktlint")` on top of Kotlin files with regions (the lint must not reformat published text). Support code is normal ktlint code.
- `@ExtendWith(LiveExtension::class)` (Java: `@ExtendWith(LiveExtension.class)`) on the class. Test parameters `LiveClients` and `Recorder` are resolved per test.
- One `OPEN`-style constant for the role. Every literal name in a region (channel, group, user id, file name) is listed in `support/resources/<Area>.kt`, the lint fails otherwise. Add the names there, never rename them in the sample.
- The group and the class are listed in `groups.json`, `coverage.json` is regenerated with `LIVE_MANIFEST_WRITE=1 scripts/dev/docs-snippets.sh lint`.

### A typical Kotlin async sample

The sample talks to `pubnub`, the recorder proxy over the client. A second client (`receiver`) proves what the service did. Arm every wait before the action.

```kotlin
@Test
fun publishRequestExecution(clients: LiveClients, recorder: Recorder) {
    val client = clients.kotlinClient(OPEN, "myUserId")      // user id as the docs show it
    val pubnub = recorder.wrap(client)
    val receiver = clients.kotlinClient(OPEN, "docs-receiver")
    val arrived = armMessage(receiver, "channelName", "This SDK rules!")  // armed before the action
    subscribeAndConnect(receiver, listOf("channelName"))                  // returns when the service confirmed
    Cleanup.record("history", "channelName")

    // snippet.publishRequestExecution
    val channel = pubnub.channel("channelName")
    channel.publish("This SDK rules!").async { result ->
        result.onFailure { exception -> /* Handle error */ }.onSuccess { value -> /* Handle result */ }
    }
    // snippet.end

    val published = recorder.awaitOk("publish") as PNPublishResult   // the recorder sees the result before the sample's callback
    val message = arrived.await() as PNMessageResult
    assertEquals("This SDK rules!", message.message.asString)
    assertEquals(published.timetoken, message.timetoken)
    storedItem(receiver, "channelName", published.timetoken)          // history lags, storedItem waits
}
```

### A typical Java sample

The sample's client is a Java client (`clients.javaClient`), wrapped with `recorder.wrap(client, PubNub.class)`. Second clients are Kotlin clients, so `Kit` (static helpers) works on them. A Java lambda cannot throw the checked `PubNubException`, so use `Kit.retry`, `Kit.cleanup` and `Kit.eventually` for setup. `JavaWait` is the twin of `Wait` for the sample's own Java client.

```java
@Test
void publishOld(LiveClients clients, Recorder recorder) throws Exception {
    PubNub client = javaClient(clients, "yourUserId");
    PubNub pubNub = recorder.wrap(client, PubNub.class);
    com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
    Wait.Expectation arrived = Kit.armMessage(receiver, "my_channel", "lat");
    Kit.subscribeAndConnect(receiver, Collections.singletonList("my_channel"));
    Cleanup.record("history", "my_channel");
    Stdout out = Stdout.captureForTest();      // for a sample that prints

    // snippet.publishOld
    ... pubNub.publish().message(position).channel("my_channel").async(result -> { ... });
    // snippet.end

    PNPublishResult published = (PNPublishResult) recorder.awaitOk("publish");
    PNMessageResult message = (PNMessageResult) arrived.await();
    assertEquals(published.getTimetoken(), message.getTimetoken());
    Wait.eventuallyRun("the sample printed the timetoken", () -> out.assertPrinted("pub timetoken: " + published.getTimetoken()));
}
```

### A "Main" sample and its caller

A whole-file sample (`samples/PublishMain.kt`, `samples/PublishApp.java`) keeps its `"demo"` keys in the visible text. One hidden line inside the config builder swaps in the live keys:

```kotlin
val config = PNConfiguration.builder(userId, "demo").apply {
    publishKey = "demo"
    // snippet.hide
    com.pubnub.docs.support.LiveKeys.apply(this, "open")
    // snippet.show
}.build()
```

The test arms a receiver, calls `main()` (Java: `PublishApp.main(new String[0])`) once, then asserts on what the receiver got and on what the sample printed (`Stdout.captureForTest()`). A Java sample that never destroys its client gets a second hidden line right after `PubNub.create`: `com.pubnub.docs.support.LiveClients.trackCurrent(pubnub);`. The client then leaves and is destroyed after the test. Note this as a docs finding.

### Placeholders

A docs sample may use a value that cannot be live (a token, a cipher key, a device id). Register it before the sample runs: `recorder.placeholder("sampleValue", liveValue)`. The recorder swaps the string at the client call boundary and the live value is redacted from every message. The visible text is unchanged.

### Presence and subscribe patterns

- Subscribe a second client with `subscribeAndConnect(client, channels, groups, presence)`. It returns when the service confirmed every name, never sleep.
- For a presence event arm first (`armPresence(client, "join", "docs-joiner", "my_channel")`), act, then `await(Wait.PRESENCE_EVENT_TIMEOUT_MS)`.
- Call `awaitNoOccupants(helperClient, channels...)` BEFORE the sample's client subscribes when the test asserts an occupancy. Called after the subscribe it sees the sample's own client.
- Use `docs-joiner`, `docs-receiver`, `docs-crowd-N` for helper users. Presence events above the announce max (20) turn into interval events, `joinCrowd` builds that.
- To prove that something does not happen, arm a control (a listener that does see events), then `Kit.assertNothingArrives` or `Kit.assertNothingHandled`. Waiting alone proves nothing.
- A client that has to disappear without a leave (a timeout test) is built with `suppressLeaveEvents = true` in its `configure` block. The leave check skips it.

### Cleanup and resources

- Every client built through `LiveClients` (and every client a test hands to `clients.track`) gets a presence timeout of 20 seconds, the SDK minimum, so a missed leave costs seconds, not the default 300.
- After each test the extension first calls `LiveClients.leaveAll`: for each client still subscribed it calls `unsubscribeAll` and then polls `hereNow` (on a watcher client) until the user is gone from every channel and group. Only then do the registered cleanups run and the clients get destroyed. The order matters. A leave through a channel group that was already deleted does not clear the presence of the channels in it. The SDK has no public leave call that can be awaited, `hereNow` is the confirmation.
- Register what the test creates with `Cleanup.record("history" | "channel_group" | "uuid_metadata" | "channel_metadata", name)` or `Cleanup.cleanupAfterTest(label) { ... }`. They run newest first.
- Channel groups created for a test: `createGroup(client, "cg1", "ch3")`.
- Clients a sample builds itself are tracked after the region: `clients.track(pubnub)`.

### What to avoid

- No assertion, wait, `Kit` or `Wait` call between a start marker and `// snippet.end`. The sample is never inside `eventually`, `settled`, a retry or a try block.
- No `@Disabled`, no assumptions, no silent skip. Label what cannot run in `snippet-exceptions.json` or with `@CompileOnly("reason")`.
- No fixed sleeps to wait for a result. `Wait.eventually` for an assertion that needs a moment, `Wait.settled` for a read after a write (a lagging replica can answer old data once), `armX` plus `await` for events.
- No key, token or secret in an assertion or log message. `assertTrue(actual == expected, "message without the value")`.
- No more than 3 hidden lines in a block, and nothing test-only in the visible text. Hidden lines only swap in the live keys or register a client.
- Never retry the sample, never weaken an assertion, never lengthen a timeout only to hide a race. A test that fails once in three runs has a race, find it (usually a one-shot read after a write that needs `settled`, or a listener armed too late).
- Do not call `check.sh format` on files with markers and do not edit a sample text. If a docs text is wrong, report it to the docs owner instead of changing the sample.
- Literal names live in `support/resources/<Area>.kt`. A name that is not listed is not cleaned.
