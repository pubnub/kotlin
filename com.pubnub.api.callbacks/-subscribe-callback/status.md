[pubnub-kotlin](../../index.md) / [com.pubnub.api.callbacks](../index.md) / [SubscribeCallback](index.md) / [status](./status.md)

# status

`abstract fun status(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`, pnStatus: `[`PNStatus`](../../com.pubnub.api.models.consumer/-p-n-status/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Receive status events like
[PNStatusCategory.PNAcknowledgmentCategory](../../com.pubnub.api.enums/-p-n-status-category/-p-n-acknowledgment-category.md),
[PNStatusCategory.PNConnectedCategory](../../com.pubnub.api.enums/-p-n-status-category/-p-n-connected-category.md),
[PNStatusCategory.PNReconnectedCategory](../../com.pubnub.api.enums/-p-n-status-category/-p-n-reconnected-category.md)

and other events related to the subscribe loop and channel mix.

### Parameters

`pubnub` - The client instance which has this listener attached.

`pnStatus` - API operation metadata.