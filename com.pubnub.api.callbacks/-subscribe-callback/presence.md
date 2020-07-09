[pubnub-kotlin](../../index.md) / [com.pubnub.api.callbacks](../index.md) / [SubscribeCallback](index.md) / [presence](./presence.md)

# presence

`open fun presence(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`, pnPresenceEventResult: `[`PNPresenceEventResult`](../../com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Receive presence events for channels subscribed to with presence enabled via `withPresence = true` in
[PubNub.subscribe](../../com.pubnub.api/-pub-nub/subscribe.md)

### Parameters

`pubnub` - The client instance which has this listener attached.

`pnPresenceEventResult` - Wrapper around a presence event.