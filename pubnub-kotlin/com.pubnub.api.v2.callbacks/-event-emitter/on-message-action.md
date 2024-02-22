//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventEmitter](index.md)/[onMessageAction](on-message-action.md)

# onMessageAction

[jvm]\
abstract var [onMessageAction](on-message-action.md): ([PNMessageActionResult](../../com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?

A nullable property that allows setting a function or lambda to react to message action events. This function is invoked whenever a new message action is received, providing a convenient way to process or react to message actions.

Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one. For scenarios requiring multiple behaviors in response to message action events, it is advisable to utilize [EventEmitter.addListener](add-listener.md).

To deactivate the current behavior, simply set this property to `null`.

**Setting a Behavior Example**:

```kotlin
onMessageAction = { pnMessageActionResult ->
    println("Message action event: ${pnMessageActionResult.data}")
}
```

**Removing a Behavior Example**:

```kotlin
onMessageAction = null
```
