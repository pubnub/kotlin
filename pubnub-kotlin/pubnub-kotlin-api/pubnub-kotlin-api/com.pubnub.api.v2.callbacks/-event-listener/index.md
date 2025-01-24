//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventListener](index.md)

# EventListener

expect interface [EventListener](index.md) : [Listener](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.callbacks/-listener/index.md)actual interface [EventListener](index.md) : [Listener](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.callbacks/-listener/index.md)actual interface [EventListener](index.md) : [Listener](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.callbacks/-listener/index.md)actual interface [EventListener](index.md) : [Listener](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.callbacks/-listener/index.md)

Implement this interface and pass it into [EventEmitter.addListener](../-event-emitter/add-listener.md) to listen for events from the PubNub real-time network.

Implement this interface and pass it into [EventEmitter.addListener](../-event-emitter/add-listener.md) to listen for events from the PubNub real-time network.

Implement this interface and pass it into [EventEmitter.addListener](../-event-emitter/add-listener.md) to listen for events from the PubNub real-time network.

Implement this interface and pass it into [EventEmitter.addListener](../../com.pubnub.api/-pub-nub/index.md#595748040%2FFunctions%2F-167468485) to listen for events from the PubNub real-time network.

#### Inheritors

| |
|---|
| [EventListenerImpl](../-event-listener-impl/index.md) |
| [SubscribeCallback](../../com.pubnub.api.callbacks/-subscribe-callback/index.md) |

## Properties

| Name | Summary |
|---|---|
| [onFile](on-file.md) | [apple]<br>abstract val [onFile](on-file.md): ([PubNub](../../com.pubnub.api/-pub-nub/index.md), [PNFileEventResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html) |
| [onMessage](on-message.md) | [apple]<br>abstract val [onMessage](on-message.md): ([PubNub](../../com.pubnub.api/-pub-nub/index.md), [PNMessageResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html) |
| [onMessageAction](on-message-action.md) | [apple]<br>abstract val [onMessageAction](on-message-action.md): ([PubNub](../../com.pubnub.api/-pub-nub/index.md), [PNMessageActionResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html) |
| [onObjects](on-objects.md) | [apple]<br>abstract val [onObjects](on-objects.md): ([PubNub](../../com.pubnub.api/-pub-nub/index.md), [PNObjectEventResult](../../com.pubnub.api.models.consumer.pubsub.objects/-p-n-object-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html) |
| [onPresence](on-presence.md) | [apple]<br>abstract val [onPresence](on-presence.md): ([PubNub](../../com.pubnub.api/-pub-nub/index.md), [PNPresenceEventResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html) |
| [onSignal](on-signal.md) | [apple]<br>abstract val [onSignal](on-signal.md): ([PubNub](../../com.pubnub.api/-pub-nub/index.md), [PNSignalResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html) |
| [underlying](underlying.md) | [apple]<br>abstract val [underlying](underlying.md): <!---  GfmCommand {"@class":"org.jetbrains.dokka.gfm.ResolveLinkGfmCommand","dri":{"packageName":"","classNames":"<Error class: unknown class>","callable":null,"target":{"@class":"org.jetbrains.dokka.links.PointingToDeclaration"},"extra":null}} --->&lt;Error class: unknown class&gt;<!--- ---> |

## Functions

| Name | Summary |
|---|---|
| [file](file.md) | [jvm]<br>open fun [file](file.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNFileEventResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md))<br>Receive file events in subscribed channels. |
| [message](message.md) | [jvm]<br>open fun [message](message.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNMessageResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md))<br>Receive messages at subscribed channels. |
| [messageAction](message-action.md) | [jvm]<br>open fun [messageAction](message-action.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNMessageActionResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md))<br>Receive message actions for messages in subscribed channels. |
| [objects](objects.md) | [jvm]<br>open fun [objects](objects.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNObjectEventResult](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.pubsub.objects/-p-n-object-event-result/index.md))<br>Receive channel metadata and UUID metadata events in subscribed channels. |
| [presence](presence.md) | [jvm]<br>open fun [presence](presence.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNPresenceEventResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md))<br>Receive presence events for channels subscribed with presence enabled via passing [com.pubnub.api.v2.subscriptions.SubscriptionOptions.receivePresenceEvents](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.subscriptions/-subscription-options/-companion/receive-presence-events.md) in [com.pubnub.api.v2.entities.Subscribable.subscription](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.entities/-subscribable/subscription.md). |
| [signal](signal.md) | [jvm]<br>open fun [signal](signal.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNSignalResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md))<br>Receive signals at subscribed channels. |
