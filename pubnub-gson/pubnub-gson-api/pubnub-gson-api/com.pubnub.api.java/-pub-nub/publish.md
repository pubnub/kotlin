//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java](../index.md)/[PubNub](index.md)/[publish](publish.md)

# publish

[jvm]\
abstract fun [publish](publish.md)(message: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html), channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [PublishBuilder](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md)

Send a message to all subscribers of a channel.

To publish a message you must first specify a valid PNConfiguration.setPublishKey. A successfully published message is replicated across the PubNub Real-Time Network and sent simultaneously to all subscribed clients on a channel.

Messages in transit can be secured from potential eavesdroppers with SSL/TLS by setting PNConfiguration.setSecure to `true` during initialization.

**Publish Anytime**

It is not required to be subscribed to a channel in order to publish to that channel.

**Message Data:**

The message argument can contain any JSON serializable data, including: Objects, Arrays, Integers and Strings. Data should not contain special Java/Kotlin classes or functions as these will not serialize. String content can include any single-byte or multi-byte UTF-8 character.

#### Return

[PublishBuilder](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md)

#### Parameters

jvm

| | |
|---|---|
| message | The payload |
| channel | The channel to publish message to. |

[jvm]\
abstract fun [~~publish~~](publish.md)(): [Publish](../../com.pubnub.api.java.endpoints.pubsub/-publish/index.md)

---

### Deprecated

Use publish(Object, String) instead

#### Replace with

```kotlin
publish(message, channel)
```
---

Send a message to all subscribers of a channel.

To publish a message you must first specify a valid PNConfiguration.setPublishKey. A successfully published message is replicated across the PubNub Real-Time Network and sent simultaneously to all subscribed clients on a channel.

Messages in transit can be secured from potential eavesdroppers with SSL/TLS by setting PNConfiguration.setSecure to `true` during initialization.

**Publish Anytime**

It is not required to be subscribed to a channel in order to publish to that channel.

**Message Data:**

The message argument can contain any JSON serializable data, including: Objects, Arrays, Integers and Strings. Data should not contain special Java/Kotlin classes or functions as these will not serialize. String content can include any single-byte or multi-byte UTF-8 character.
