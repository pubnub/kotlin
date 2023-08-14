//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.callbacks](../index.md)/[SubscribeCallback](index.md)

# SubscribeCallback

[jvm]\
abstract class [SubscribeCallback](index.md) : [Listener](../-listener/index.md)

## Constructors

| | |
|---|---|
| [SubscribeCallback](-subscribe-callback.md) | [jvm]<br>fun [SubscribeCallback](-subscribe-callback.md)() |

## Functions

| Name | Summary |
|---|---|
| [file](file.md) | [jvm]<br>open fun [file](file.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), pnFileEventResult: [PNFileEventResult](../../com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md)) |
| [message](message.md) | [jvm]<br>open fun [message](message.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), pnMessageResult: [PNMessageResult](../../com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md))<br>Receive messages at subscribed channels. |
| [messageAction](message-action.md) | [jvm]<br>open fun [messageAction](message-action.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), pnMessageActionResult: [PNMessageActionResult](../../com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md))<br>Receive message actions for messages in subscribed channels. |
| [objects](objects.md) | [jvm]<br>open fun [objects](objects.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), objectEvent: [PNObjectEventResult](../../com.pubnub.api.models.consumer.pubsub.objects/-p-n-object-event-result/index.md)) |
| [presence](presence.md) | [jvm]<br>open fun [presence](presence.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), pnPresenceEventResult: [PNPresenceEventResult](../../com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md))<br>Receive presence events for channels subscribed to with presence enabled via `withPresence = true` in [PubNub.subscribe](../../com.pubnub.api/-pub-nub/subscribe.md) |
| [signal](signal.md) | [jvm]<br>open fun [signal](signal.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), pnSignalResult: [PNSignalResult](../../com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md))<br>Receive signals at subscribed channels. |
| [status](status.md) | [jvm]<br>abstract fun [status](status.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), pnStatus: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md))<br>Receive status events like [PNStatusCategory.PNAcknowledgmentCategory](../../com.pubnub.api.enums/-p-n-status-category/-p-n-acknowledgment-category/index.md), [PNStatusCategory.PNConnectedCategory](../../com.pubnub.api.enums/-p-n-status-category/-p-n-connected-category/index.md), [PNStatusCategory.PNReconnectedCategory](../../com.pubnub.api.enums/-p-n-status-category/-p-n-reconnected-category/index.md) |
