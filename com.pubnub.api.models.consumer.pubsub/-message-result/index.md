[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.pubsub](../index.md) / [MessageResult](./index.md)

# MessageResult

`open class MessageResult : `[`BasePubSubResult`](../-base-pub-sub-result/index.md)

### Properties

| Name | Summary |
|---|---|
| [message](message.md) | The actual message content`val message: JsonElement` |

### Inheritors

| Name | Summary |
|---|---|
| [PNMessageResult](../-p-n-message-result.md) | Wrapper around an actual message received in [SubscribeCallback.message](../../com.pubnub.api.callbacks/-subscribe-callback/message.md).`class PNMessageResult : `[`MessageResult`](./index.md) |
| [PNSignalResult](../-p-n-signal-result/index.md) | Wrapper around a signal received in [SubscribeCallback.signal](../../com.pubnub.api.callbacks/-subscribe-callback/signal.md).`class PNSignalResult : `[`MessageResult`](./index.md) |
