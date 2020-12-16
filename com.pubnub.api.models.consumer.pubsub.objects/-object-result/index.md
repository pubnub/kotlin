[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.pubsub.objects](../index.md) / [ObjectResult](./index.md)

# ObjectResult

`interface ObjectResult<T>`

### Properties

| Name | Summary |
|---|---|
| [data](data.md) | `abstract val data: T` |
| [event](event.md) | `abstract val event: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [PNMessageActionResult](../../com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md) | Wrapper around message actions received in [SubscribeCallback.messageAction](../../com.pubnub.api.callbacks/-subscribe-callback/message-action.md).`data class PNMessageActionResult : `[`ObjectResult`](./index.md)`<`[`PNMessageAction`](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md)`>, `[`PubSubResult`](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/index.md) |
