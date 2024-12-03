### PubNub Java and Kotlin-based SDKs for Android

[![Tests](https://github.com/pubnub/kotlin/actions/workflows/run-tests.yml/badge.svg)](https://github.com/pubnub/kotlin/actions/workflows/run-tests.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.pubnub/pubnub-kotlin.svg)](https://maven-badges.herokuapp.com/maven-central/com.pubnub/pubnub-kotlin)

This is the official PubNub Kotlin SDK repository.

PubNub takes care of the infrastructure and APIs needed for the realtime communication layer of your application. Work on your app's logic and let PubNub handle sending and receiving data across the world in less than 100ms.

## Get keys

You will need the publish and subscribe keys to authenticate your app. Get your keys from the [Admin Portal](https://dashboard.pubnub.com/login).

## Configure PubNub

1. Integrate the Kotlin SDK into your project:

   * for Maven, add the following dependency in your `pom.xml`:
     ```xml
     <dependency>
        <groupId>com.pubnub</groupId>
        <artifactId>pubnub-kotlin</artifactId>
        <version>10.2.1</version>
     </dependency>
     ```

   * for Gradle, add the following dependency in your `gradle.build`:
     ```groovy
     implementation ("com.pubnub:pubnub-kotlin:10.0.0")
     ```

2. Configure your keys and create PubNub instance:

    ```kotlin
    val config = PNConfiguration.builder(UserId("myUserId"), "mySubKey") {
        publishKey = "myPubKey"
    }
    val pubnub = PubNub.create(config.build())
    ```

## Add event listeners

```kotlin
pubnub.addListener(object : SubscribeCallback() {

    override fun status(pubnub: PubNub, status: PNStatus) {
        println("Status category: ${status.category}")
        // PNConnectedCategory, PNReconnectedCategory, PNDisconnectedCategory

        println("Status operation: ${status.operation}")
        // PNSubscribeOperation, PNHeartbeatOperation

        println("Status error: ${status.error}")
        // true or false
    }

    override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
        println("Presence event: ${pnPresenceEventResult.event}")
        println("Presence channel: ${pnPresenceEventResult.channel}")
        println("Presence uuid: ${pnPresenceEventResult.uuid}")
        println("Presence timetoken: ${pnPresenceEventResult.timetoken}")
        println("Presence occupancy: ${pnPresenceEventResult.occupancy}")
    }

    override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
        println("Message payload: ${pnMessageResult.message}")
        println("Message channel: ${pnMessageResult.channel}")
        println("Message publisher: ${pnMessageResult.publisher}")
        println("Message timetoken: ${pnMessageResult.timetoken}")
    }

    override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
        println("Signal payload: ${pnSignalResult.message}")
        println("Signal channel: ${pnSignalResult.channel}")
        println("Signal publisher: ${pnSignalResult.publisher}")
        println("Signal timetoken: ${pnSignalResult.timetoken}")
    }

    override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {
        with(pnMessageActionResult.messageAction) {
            println("Message action type: $type")
            println("Message action value: $value")
            println("Message action uuid: $uuid")
            println("Message action actionTimetoken: $actionTimetoken")
            println("Message action messageTimetoken: $messageTimetoken")
        }

        println("Message action subscriptions: ${pnMessageActionResult.subscription}")
        println("Message action channel: ${pnMessageActionResult.channel}")
        println("Message action timetoken: ${pnMessageActionResult.timetoken}")
    }
})
```

## Publish/subscribe

```kotlin
pubnub.publish(channel = "my_channel", message = "hello")
    .async { result -> 
    // the result is always of a nullable type
    // it's null if there were errors (status.error)
    // otherwise it's usable

    // handle publish result
    result.onSuccess { res ->
        println("Message timetoken: ${res!!.timetoken}")
    }.onFailure { exception ->
        // handle error
        exception.printStackTrace()
    }
}

pubnub.subscribe(channels = listOf("my_channel"))
```

## Documentation

* [API reference for Kotlin ](https://www.pubnub.com/docs/sdks/kotlin)
* [API reference for Java ](https://www.pubnub.com/docs/sdks/java)

## Support

If you **need help** or have a **general question**, contact support@pubnub.com.
