//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventListener](index.md)/[presence](presence.md)

# presence

[jvm]\
open fun [presence](presence.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), result: [PNPresenceEventResult](../../com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md))

Receive presence events for channels subscribed with presence enabled via passing [com.pubnub.api.v2.subscriptions.SubscriptionOptions.receivePresenceEvents](../../com.pubnub.api.v2.subscriptions/-subscription-options/-companion/receive-presence-events.md) in [com.pubnub.api.v2.entities.Subscribable.subscription](../../com.pubnub.api.v2.entities/-subscribable/subscription.md).

#### Parameters

jvm

| | |
|---|---|
| pubnub | The client instance which has this listener attached. |
| result | Wrapper around a presence event. |
