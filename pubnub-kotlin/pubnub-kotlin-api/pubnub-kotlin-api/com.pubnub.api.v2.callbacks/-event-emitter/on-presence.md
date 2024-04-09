//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventEmitter](index.md)/[onPresence](on-presence.md)

# onPresence

[jvm]\
abstract var [onPresence](on-presence.md): ([PNPresenceEventResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?

A nullable property designed to set a function or lambda expression for handling incoming presence events. This function is called whenever a new presence event occurs, offering an efficient method for tracking presence updates.

Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one. For scenarios requiring multiple behaviors in response to presence events, it is advisable to utilize [EventEmitter.addListener](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.v2.callbacks/-event-emitter/add-listener.md).

To deactivate the current behavior, simply set this property to `null`.

**Setting a Behavior Example**:

```kotlin
onPresence = { pnPresenceEventResult ->
    println("Presence event: ${pnPresenceEventResult.event}")
}
```

**Removing a Behavior Example**:

```kotlin
onPresence = null
```
