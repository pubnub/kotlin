//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventEmitter](index.md)/[onObjects](on-objects.md)

# onObjects

[jvm]\
abstract var [onObjects](on-objects.md): ([PNObjectEventResult](../../com.pubnub.api.models.consumer.pubsub.objects/-p-n-object-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?

A nullable property for assigning a function or lambda to handle object events. This function is triggered with each new object event, providing a mechanism to manage object-related updates.

Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one. For scenarios requiring multiple behaviors in response to object event, it is advisable to utilize [EventEmitter.addListener](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-event-emitter/add-listener.md).

To deactivate the current behavior, simply set this property to `null`.

**Setting a Behavior Example**:

```kotlin
onObjects = { pnObjectEventResult ->
    println("Object event: ${pnObjectEventResult.result}")
}
```

**Removing a Behavior Example**:

```kotlin
onObjects = null
```
