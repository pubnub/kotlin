<img width="1920" height="600" alt="PubNub Java and Kotlin SDK header: a publish call sends Hello world! to the hello_world channel" src="https://github.com/user-attachments/assets/8eb62cc0-2c3a-4666-a3a4-3d73319bd152" />

# PubNub Java and Kotlin SDK

[![Java Maven Central](https://img.shields.io/maven-central/v/com.pubnub/pubnub-gson.svg?label=Java)](https://central.sonatype.com/artifact/com.pubnub/pubnub-gson) [![Kotlin Maven Central](https://img.shields.io/maven-central/v/com.pubnub/pubnub-kotlin.svg?label=Kotlin)](https://central.sonatype.com/artifact/com.pubnub/pubnub-kotlin)

PubNub provides global infrastructure for real-time, interactive applications.

Publish and receive messages in Java and Kotlin. Use this SDK for Java and Kotlin applications on Android, JVM servers, and desktop environments.

[Java documentation](https://www.pubnub.com/docs/sdks/java) · [Kotlin documentation](https://www.pubnub.com/docs/sdks/kotlin) · [Java API reference](https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe) · [Kotlin API reference](https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe) · [Java changelog](https://www.pubnub.com/docs/sdks/java/changelog) · [Kotlin changelog](https://www.pubnub.com/docs/sdks/kotlin/changelog) · [Support](https://support.pubnub.com)

## Requirements

| Requirement | Supported version or setup |
|---|---|
| Java runtime | Java 8 or later |
| Kotlin (Kotlin projects only) | Kotlin 2.1 or later. The SDK is built with Kotlin 2.2. |
| Target platforms | [Java/JVM and Android](https://www.pubnub.com/docs/sdks/java) · [Kotlin/JVM and Android](https://www.pubnub.com/docs/sdks/kotlin) |
| Android network access | For Android, add `<uses-permission android:name="android.permission.INTERNET" />` to `AndroidManifest.xml`. |

## Installation

Choose the dependency for your language and add it to the `dependencies` block of your application's Gradle build file:

```kotlin
// Java
implementation("com.pubnub:pubnub-gson:14.1.0")
// Kotlin
implementation("com.pubnub:pubnub-kotlin:14.1.0")
```

For Maven, add the dependency for your language to `pom.xml`.

Java:

```xml
<dependency>
    <groupId>com.pubnub</groupId>
    <artifactId>pubnub-gson</artifactId>
    <version>14.1.0</version>
</dependency>
```

Kotlin:

```xml
<dependency>
    <groupId>com.pubnub</groupId>
    <artifactId>pubnub-kotlin</artifactId>
    <version>14.1.0</version>
</dependency>
```

For other installation methods, see [Java environment setup](https://www.pubnub.com/docs/sdks/java) or [Kotlin environment setup](https://www.pubnub.com/docs/sdks/kotlin).

## Quickstart

This example runs in a Java or Kotlin JVM application. It subscribes to `hello_world`, publishes one message, and prints the received text.

### Get your keys

1. Open the [PubNub Admin Portal](https://admin.pubnub.com).
2. Create an app and a keyset for development, or select an existing development keyset.
3. Copy its publish key and subscribe key.

If Access Manager is enabled, obtain a token from your trusted backend and set it on the client before accessing protected resources:

- Java: `pubnub.setToken(token);`
- Kotlin: `pubnub.setToken(token)`

Keep the secret key and token-granting logic on the backend. Never include the secret key in a distributed client.

### Send and receive a message

Choose your language. In a Gradle project with the PubNub dependency, apply the `application` plugin and set its main class: `mainClass.set("App")` for Java or `mainClass.set("AppKt")` for Kotlin. Create `App.java` in `src/main/java` or `App.kt` in `src/main/kotlin` and add the corresponding example below.

Replace the key placeholders. Use a User ID that identifies the user or device in your app.

Java — `App.java`:

```java
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.java.v2.entities.Channel;
import com.pubnub.api.java.v2.subscriptions.Subscription;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class App {
    public static void main(String[] args) throws Exception {
        PNConfiguration configuration = PNConfiguration
                .builder(new UserId("hello-world-java"), "YOUR_SUBSCRIBE_KEY")
                .publishKey("YOUR_PUBLISH_KEY")
                .build();
        PubNub pubnub = PubNub.create(configuration);

        Channel channel = pubnub.channel("hello_world");
        Subscription subscription = channel.subscription();
        CountDownLatch received = new CountDownLatch(1);
        AtomicBoolean published = new AtomicBoolean(false);

        subscription.setOnMessage(message -> {
            System.out.println(message.getMessage().getAsString());
            received.countDown();
        });

        pubnub.addListener((client, status) -> {
            if (status.getCategory() == PNStatusCategory.PNConnectedCategory
                    && published.compareAndSet(false, true)) {
                channel.publish("Hello world").async(result ->
                        result.onFailure(exception -> exception.printStackTrace()));
            }
        });

        try {
            subscription.subscribe();
            if (!received.await(15, TimeUnit.SECONDS)) {
                throw new RuntimeException("Timed out waiting for message");
            }
        } finally {
            subscription.close();
            pubnub.destroy();
        }
    }
}
```

Kotlin — `App.kt`:

```kotlin
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.v2.callbacks.StatusListener
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

fun main() {
    val pubnub = PubNub.create(UserId("hello-world-kotlin"), "YOUR_SUBSCRIBE_KEY") {
        publishKey = "YOUR_PUBLISH_KEY"
    }

    val channel = pubnub.channel("hello_world")
    val subscription = channel.subscription()
    val received = CountDownLatch(1)
    val published = AtomicBoolean(false)

    subscription.onMessage = { message ->
        println(message.message.asString)
        received.countDown()
    }

    pubnub.addListener(object : StatusListener {
        override fun status(pubnub: PubNub, status: PNStatus) {
            if (status.category == PNStatusCategory.PNConnectedCategory && published.compareAndSet(false, true)) {
                channel.publish("Hello world").async { result ->
                    result.onFailure { exception -> exception.printStackTrace() }
                }
            }
        }
    })

    try {
        subscription.subscribe()
        check(received.await(15, TimeUnit.SECONDS)) { "Timed out waiting for message" }
    } finally {
        subscription.close()
        pubnub.destroy()
    }
}
```

### Run the example

```bash
./gradlew run
```

The terminal should show:

```text
Hello world
```

Both examples call `subscription.close()` to unsubscribe and release the subscription's listeners, followed by `pubnub.destroy()` to release the client.

For a complete application, see the [Java getting started guide](https://www.pubnub.com/docs/sdks/java) or [Kotlin getting started guide](https://www.pubnub.com/docs/sdks/kotlin).

## Next steps

| Task | Documentation |
|---|---|
| Configure the client | [Java](https://www.pubnub.com/docs/sdks/java/api-reference/configuration) · [Kotlin](https://www.pubnub.com/docs/sdks/kotlin/api-reference/configuration) |
| Work with subscriptions and messages | [Java](https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe) · [Kotlin](https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe) |
| Check channel occupancy | [Java](https://www.pubnub.com/docs/sdks/java/api-reference/presence) · [Kotlin](https://www.pubnub.com/docs/sdks/kotlin/api-reference/presence) |
| Read message history | [Java](https://www.pubnub.com/docs/sdks/java/api-reference/storage-and-playback) · [Kotlin](https://www.pubnub.com/docs/sdks/kotlin/api-reference/storage-and-playback) |
| Secure resources | [Java](https://www.pubnub.com/docs/sdks/java/api-reference/access-manager) · [Kotlin](https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager) |

## Build with an AI coding assistant

The [PubNub MCP server](https://github.com/pubnub/pubnub-mcp-server) gives an AI coding assistant access to PubNub SDK documentation and PubNub APIs. Connect the assistant to the hosted server at `https://mcp.pubnub.com`, or run `npx @pubnub/mcp@latest` locally.

The [server repository](https://github.com/pubnub/pubnub-mcp-server) has setup steps for VS Code, Cursor, Claude Code, Claude Desktop, Codex, Gemini CLI, and OpenCode.

## Before production

Use Access Manager ([Java](https://www.pubnub.com/docs/sdks/java/api-reference/access-manager) · [Kotlin](https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager)) to grant each client the permissions it needs. Keep the secret key on your backend. Never include it in a distributed client.

Create one PubNub client per user or application session instead of one per operation. Java and Kotlin entity subscriptions implement `AutoCloseable`; call `subscription.close()` to unsubscribe and remove its listeners, or `pubnub.unsubscribeAll()` to stop every subscription. Call `pubnub.destroy()` when the owning client is no longer needed.

Live delivery through PubNub SDKs is at-most-once. A subscriber can miss messages if its buffer overflows or if it is disconnected when a message is published. For recovery after longer gaps, see Message Persistence for [Java](https://www.pubnub.com/docs/sdks/java/api-reference/storage-and-playback) or [Kotlin](https://www.pubnub.com/docs/sdks/kotlin/api-reference/storage-and-playback).

## Troubleshooting

| Symptom | Check |
|---|---|
| A Java application fails with `java.lang.NoClassDefFoundError: kotlin/enums/EnumEntriesKt` | An older Kotlin standard library on the classpath overrides the one the SDK requires. Align `org.jetbrains.kotlin:kotlin-stdlib` with the version the SDK depends on. See [Java troubleshooting](https://www.pubnub.com/docs/sdks/java/troubleshooting). |
| A publish succeeds but no message appears | Check the message handler, subscription readiness, keyset, and channel name. |
| An Android release build crashes with `NoSuchMethodError` after R8 or ProGuard processing | Add `-keepnames class org.json.** { *; }` to `proguard-rules.pro`. See [Kotlin troubleshooting](https://www.pubnub.com/docs/sdks/kotlin/troubleshoot). |
| Extra clients or duplicate messages during development | Reuse the PubNub client at application or session scope instead of recreating it with every Activity, Fragment, or Compose recomposition. Close subscriptions owned by terminated components and call `pubnub.destroy()` when the client's owner terminates. |

For setup help, see [Java troubleshooting](https://www.pubnub.com/docs/sdks/java/troubleshooting) or [Kotlin troubleshooting](https://www.pubnub.com/docs/sdks/kotlin/troubleshoot). Check [network status](https://status.pubnub.com) for service incidents.

## Releases

Read the [Java changelog](https://www.pubnub.com/docs/sdks/java/changelog) or [Kotlin changelog](https://www.pubnub.com/docs/sdks/kotlin/changelog) before upgrading.

The Java and Kotlin SDKs share a codebase and release history. When upgrading across major versions, follow the [v9 migration guide](https://www.pubnub.com/docs/sdks/kotlin/migration-guides/kotlin-v9-migration-guide), which applies to both SDKs, and review the changelogs for later breaking changes.

## Support and contributions

For setup or account help, contact [PubNub Support](https://support.pubnub.com).

For a reproducible SDK bug, use [GitHub Issues](https://github.com/pubnub/kotlin/issues). Include the SDK version, runtime, and a small reproduction with credentials removed.

Building the repository requires Java 21. Before opening a pull request against `master`, run `./gradlew check`, which runs unit tests and static analysis across all modules.

## License

See the [PubNub Software Development Kit License Agreement](https://github.com/pubnub/kotlin/blob/master/LICENSE).
