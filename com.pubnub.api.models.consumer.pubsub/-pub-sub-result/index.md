[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.pubsub](../index.md) / [PubSubResult](./index.md)

# PubSubResult

`interface PubSubResult`

### Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | `abstract val channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [publisher](publisher.md) | `abstract val publisher: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [subscription](subscription.md) | `abstract val subscription: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [timetoken](timetoken.md) | `abstract val timetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
| [userMetadata](user-metadata.md) | `abstract val userMetadata: JsonElement?` |

### Inheritors

| Name | Summary |
|---|---|
| [BasePubSubResult](../-base-pub-sub-result/index.md) | `data class BasePubSubResult : `[`PubSubResult`](./index.md) |
| [MessageResult](../-message-result/index.md) | `interface MessageResult : `[`PubSubResult`](./index.md) |
| [PNMessageActionResult](../../com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md) | Wrapper around message actions received in [SubscribeCallback.messageAction](../../com.pubnub.api.callbacks/-subscribe-callback/message-action.md).`data class PNMessageActionResult : `[`ObjectResult`](../../com.pubnub.api.models.consumer.pubsub.objects/-object-result/index.md)`<`[`PNMessageAction`](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md)`>, `[`PubSubResult`](./index.md) |
| [PNMessageResult](../-p-n-message-result/index.md) | Wrapper around an actual message received in [SubscribeCallback.message](../../com.pubnub.api.callbacks/-subscribe-callback/message.md).`data class PNMessageResult : `[`MessageResult`](../-message-result/index.md)`, `[`PubSubResult`](./index.md) |
| [PNObjectEventResult](../../com.pubnub.api.models.consumer.pubsub.objects/-p-n-object-event-result/index.md) | `data class PNObjectEventResult : `[`PubSubResult`](./index.md) |
| [PNSignalResult](../-p-n-signal-result/index.md) | Wrapper around a signal received in [SubscribeCallback.signal](../../com.pubnub.api.callbacks/-subscribe-callback/signal.md).`data class PNSignalResult : `[`MessageResult`](../-message-result/index.md)`, `[`PubSubResult`](./index.md) |
