//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventEmitter](index.md)/[onMessage](on-message.md)

# onMessage

[common]\
open var [onMessage](on-message.md): ([PNMessageResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)?

A nullable property that can be set to a function (or lambda expression) to handle incoming message events. This function is invoked whenever a new message is received, providing a convenient way to process or react to messages.

Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one. For scenarios requiring multiple behaviors in response to message events, it is advisable to utilize [EventEmitter.addListener](add-listener.md).

To deactivate the current behavior, simply set this property to `null`.

**Setting a Behavior Example**:

```kotlin
onMessage = { pnMessageResult ->
    println("Received message: ${pnMessageResult.message}")
}
```

**Removing a Behavior Example**:

```kotlin
onMessage = null
```
