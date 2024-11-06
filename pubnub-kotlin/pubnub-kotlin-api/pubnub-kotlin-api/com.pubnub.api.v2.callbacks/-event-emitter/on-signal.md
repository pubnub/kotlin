//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventEmitter](index.md)/[onSignal](on-signal.md)

# onSignal

[common]\
open var [onSignal](on-signal.md): ([PNSignalResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?

A nullable property for assigning a function or lambda expression to handle incoming signal events. This function is called whenever a new signal is received, providing a convenient way to process or react to signals.

Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one. For scenarios requiring multiple behaviors in response to signal events, it is advisable to utilize [EventEmitter.addListener](add-listener.md).

To deactivate a behavior, assign `null` to this property.

**Setting a Behavior Example**:

```kotlin
onSignal = { pnSignalResult ->
    println("Received signal: ${pnSignalResult.message}")
}
```

**Removing a Behavior Example**:

```kotlin
onSignal = null
```
