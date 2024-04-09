//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.callbacks](../index.md)/[SubscribeCallback](index.md)

# SubscribeCallback

[jvm]\
abstract class [SubscribeCallback](index.md) : [StatusListener](../../com.pubnub.api.v2.callbacks/-status-listener/index.md), [EventListener](../../com.pubnub.api.v2.callbacks/-event-listener/index.md)

A combined listener for status and event updates. Included for backward compatibility with previous versions of the PubNub SDK.

Setting explicit [StatusListener](../../com.pubnub.api.v2.callbacks/-status-listener/index.md) and/or [EventListener](../../com.pubnub.api.v2.callbacks/-event-listener/index.md) should be preferred.

## Constructors

| | |
|---|---|
| [SubscribeCallback](-subscribe-callback.md) | [jvm]<br>constructor() |

## Functions

| Name | Summary |
|---|---|
| [file](../../com.pubnub.api.v2.callbacks/-event-listener/file.md) | [jvm]<br>open fun [file](../../com.pubnub.api.v2.callbacks/-event-listener/file.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNFileEventResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md))<br>Receive file events in subscribed channels. |
| [message](../../com.pubnub.api.v2.callbacks/-event-listener/message.md) | [jvm]<br>open fun [message](../../com.pubnub.api.v2.callbacks/-event-listener/message.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNMessageResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md))<br>Receive messages at subscribed channels. |
| [messageAction](../../com.pubnub.api.v2.callbacks/-event-listener/message-action.md) | [jvm]<br>open fun [messageAction](../../com.pubnub.api.v2.callbacks/-event-listener/message-action.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNMessageActionResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md))<br>Receive message actions for messages in subscribed channels. |
| [objects](../../com.pubnub.api.v2.callbacks/-event-listener/objects.md) | [jvm]<br>open fun [objects](../../com.pubnub.api.v2.callbacks/-event-listener/objects.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNObjectEventResult](../../com.pubnub.api.models.consumer.pubsub.objects/-p-n-object-event-result/index.md))<br>Receive channel metadata and UUID metadata events in subscribed channels. |
| [presence](../../com.pubnub.api.v2.callbacks/-event-listener/presence.md) | [jvm]<br>open fun [presence](../../com.pubnub.api.v2.callbacks/-event-listener/presence.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNPresenceEventResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md))<br>Receive presence events for channels subscribed with presence enabled via passing [com.pubnub.api.v2.subscriptions.SubscriptionOptions.receivePresenceEvents](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.subscriptions/-subscription-options/-companion/receive-presence-events.md) in [com.pubnub.api.v2.entities.Subscribable.subscription](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.entities/-subscribable/subscription.md). |
| [signal](../../com.pubnub.api.v2.callbacks/-event-listener/signal.md) | [jvm]<br>open fun [signal](../../com.pubnub.api.v2.callbacks/-event-listener/signal.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNSignalResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md))<br>Receive signals at subscribed channels. |
| [status](../../com.pubnub.api.v2.callbacks/-status-listener/status.md) | [jvm]<br>abstract fun [status](../../com.pubnub.api.v2.callbacks/-status-listener/status.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), status: [PNStatus](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer/-p-n-status/index.md))<br>Receive status updates from the PubNub client, such as: |
