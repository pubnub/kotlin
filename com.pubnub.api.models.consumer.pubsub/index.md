[pubnub-kotlin](../index.md) / [com.pubnub.api.models.consumer.pubsub](./index.md)

## Package com.pubnub.api.models.consumer.pubsub

### Types

| Name | Summary |
|---|---|
| [BasePubSubResult](-base-pub-sub-result/index.md) | `open class BasePubSubResult` |
| [MessageResult](-message-result/index.md) | `open class MessageResult : `[`BasePubSubResult`](-base-pub-sub-result/index.md) |
| [PNMessageResult](-p-n-message-result.md) | Wrapper around an actual message received in [SubscribeCallback.message](../com.pubnub.api.callbacks/-subscribe-callback/message.md).`class PNMessageResult : `[`MessageResult`](-message-result/index.md) |
| [PNPresenceEventResult](-p-n-presence-event-result/index.md) | Wrapper around a presence event received in [SubscribeCallback.presence](../com.pubnub.api.callbacks/-subscribe-callback/presence.md).`class PNPresenceEventResult` |
| [PNSignalResult](-p-n-signal-result/index.md) | Wrapper around a signal received in [SubscribeCallback.signal](../com.pubnub.api.callbacks/-subscribe-callback/signal.md).`class PNSignalResult : `[`MessageResult`](-message-result/index.md) |
