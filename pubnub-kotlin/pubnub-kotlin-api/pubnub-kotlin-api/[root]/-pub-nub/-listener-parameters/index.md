//[pubnub-kotlin-api](../../../../index.md)/[[root]](../../index.md)/[PubNub](../index.md)/[ListenerParameters](index.md)

# ListenerParameters

[js]\
interface [ListenerParameters](index.md)

## Properties

| Name | Summary |
|---|---|
| [file](file.md) | [js]<br>abstract val [file](file.md): (fileEvent: [PubNub.FileEvent](../-file-event/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? |
| [message](message.md) | [js]<br>abstract val [message](message.md): (messageEvent: [PubNub.MessageEvent](../-message-event/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? |
| [messageAction](message-action.md) | [js]<br>abstract val [messageAction](message-action.md): (messageActionEvent: [PubNub.MessageActionEvent](../-message-action-event/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? |
| [objects](objects.md) | [js]<br>abstract val [objects](objects.md): (objectEvent: [PubNub.BaseObjectsEvent](../-base-objects-event/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? |
| [presence](presence.md) | [js]<br>abstract val [presence](presence.md): (presenceEvent: [PubNub.PresenceEvent](../-presence-event/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? |
| [signal](signal.md) | [js]<br>abstract val [signal](signal.md): (signalEvent: [PubNub.SignalEvent](../-signal-event/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? |
