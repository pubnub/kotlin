//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventListener](index.md)

# EventListener

interface [EventListener](index.md) : [BaseEventListener](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-base-event-listener/index.md)

Implement this interface and pass it into [EventEmitter.addListener](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.v2.callbacks/-event-emitter/add-listener.md) to listen for events from the PubNub real-time network.

#### Inheritors

| |
|---|
| [SubscribeCallback](../../com.pubnub.api.callbacks/-subscribe-callback/index.md) |

## Functions

| Name | Summary |
|---|---|
| [file](file.md) | [jvm]<br>open fun [file](file.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNFileEventResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md))<br>Receive file events in subscribed channels. |
| [message](message.md) | [jvm]<br>open fun [message](message.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNMessageResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md))<br>Receive messages at subscribed channels. |
| [messageAction](message-action.md) | [jvm]<br>open fun [messageAction](message-action.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNMessageActionResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md))<br>Receive message actions for messages in subscribed channels. |
| [objects](objects.md) | [jvm]<br>open fun [objects](objects.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNObjectEventResult](../../com.pubnub.api.models.consumer.pubsub.objects/-p-n-object-event-result/index.md))<br>Receive channel metadata and UUID metadata events in subscribed channels. |
| [presence](presence.md) | [jvm]<br>open fun [presence](presence.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNPresenceEventResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md))<br>Receive presence events for channels subscribed with presence enabled via passing [com.pubnub.api.v2.subscriptions.SubscriptionOptions.receivePresenceEvents](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.subscriptions/-subscription-options/-companion/receive-presence-events.md) in [com.pubnub.api.v2.entities.Subscribable.subscription](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.entities/-subscribable/subscription.md). |
| [signal](signal.md) | [jvm]<br>open fun [signal](signal.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNSignalResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md))<br>Receive signals at subscribed channels. |
