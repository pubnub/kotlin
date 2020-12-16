[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.pubsub.message_actions](../index.md) / [PNMessageActionResult](./index.md)

# PNMessageActionResult

`data class PNMessageActionResult : `[`ObjectResult`](../../com.pubnub.api.models.consumer.pubsub.objects/-object-result/index.md)`<`[`PNMessageAction`](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md)`>, `[`PubSubResult`](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/index.md)

Wrapper around message actions received in [SubscribeCallback.messageAction](../../com.pubnub.api.callbacks/-subscribe-callback/message-action.md).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Wrapper around message actions received in [SubscribeCallback.messageAction](../../com.pubnub.api.callbacks/-subscribe-callback/message-action.md).`PNMessageActionResult(result: `[`BasePubSubResult`](../../com.pubnub.api.models.consumer.pubsub/-base-pub-sub-result/index.md)`, event: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, data: `[`PNMessageAction`](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md)`)` |

### Properties

| Name | Summary |
|---|---|
| [data](data.md) | The actual message action.`val data: `[`PNMessageAction`](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md) |
| [event](event.md) | The message action event. Could be `added` or `removed`.`val event: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [messageAction](message-action.md) | `val messageAction: `[`PNMessageAction`](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md) |
