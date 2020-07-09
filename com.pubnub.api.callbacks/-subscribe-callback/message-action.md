[pubnub-kotlin](../../index.md) / [com.pubnub.api.callbacks](../index.md) / [SubscribeCallback](index.md) / [messageAction](./message-action.md)

# messageAction

`open fun messageAction(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`, pnMessageActionResult: `[`PNMessageActionResult`](../../com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Receive message actions for messages in subscribed channels.

### Parameters

`pubnub` - The client instance which has this listener attached.

`pnMessageActionResult` - Wrapper around a message action event.