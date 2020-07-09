[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.pubsub.objects](../index.md) / [ObjectResult](./index.md)

# ObjectResult

`open class ObjectResult<T> : `[`BasePubSubResult`](../../com.pubnub.api.models.consumer.pubsub/-base-pub-sub-result/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ObjectResult(result: `[`BasePubSubResult`](../../com.pubnub.api.models.consumer.pubsub/-base-pub-sub-result/index.md)`, event: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, data: T)` |

### Inheritors

| Name | Summary |
|---|---|
| [PNMessageActionResult](../../com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md) | Wrapper around message actions received in [SubscribeCallback.messageAction](../../com.pubnub.api.callbacks/-subscribe-callback/message-action.md).`class PNMessageActionResult : `[`ObjectResult`](./index.md)`<`[`PNMessageAction`](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md)`>` |
