//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventEmitter](index.md)/[onFile](on-file.md)

# onFile

[common]\
open var [onFile](on-file.md): ([PNFileEventResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?

A nullable property to set a function or lambda for responding to file events. This function is invoked whenever a new file event is received, providing a convenient way to process or react to file events.

Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one. For scenarios requiring multiple behaviors in response to file event, it is advisable to utilize [EventEmitter.addListener](add-listener.md).

To deactivate the current behavior, simply set this property to `null`.

**Setting a Behavior Example**:

```kotlin
onFile = { pnFileEventResult ->
    println("File event: ${pnFileEventResult.message}")
}
```

**Removing a Behavior Example**:

```kotlin
onFile = null
```
