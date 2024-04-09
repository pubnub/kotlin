//[pubnub-gson-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[fire](fire.md)

# fire

[jvm]\
abstract fun [fire](fire.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PublishBuilder](../../com.pubnub.api.v2.endpoints.pubsub/-publish-builder/index.md)

Send a message to PubNub Functions Event Handlers.

These messages will go directly to any Event Handlers registered on the channel that you fire to and will trigger their execution. The content of the fired request will be available for processing within the Event Handler.

The message sent via `fire()` isn't replicated, and so won't be received by any subscribers to the channel. The message is also not stored in history.

[jvm]\
abstract fun [~~fire~~](fire.md)(): [Publish](../../com.pubnub.api.endpoints.pubsub/-publish/index.md)

---

### Deprecated

Use publish(Object, String) instead

#### Replace with

```kotlin
publish(message, channel)
```
---

Send a message to PubNub Functions Event Handlers.

These messages will go directly to any Event Handlers registered on the channel that you fire to and will trigger their execution. The content of the fired request will be available for processing within the Event Handler.

The message sent via `fire()` isn't replicated, and so won't be received by any subscribers to the channel. The message is also not stored in history.
