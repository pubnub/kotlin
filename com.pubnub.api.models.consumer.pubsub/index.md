[pubnub-kotlin](../index.md) / [com.pubnub.api.models.consumer.pubsub](./index.md)

## Package com.pubnub.api.models.consumer.pubsub

### Types

| Name | Summary |
|---|---|
| [BasePubSubResult](-base-pub-sub-result/index.md) | `data class BasePubSubResult : `[`PubSubResult`](-pub-sub-result/index.md) |
| [MessageResult](-message-result/index.md) | `interface MessageResult : `[`PubSubResult`](-pub-sub-result/index.md) |
| [PNEvent](-p-n-event.md) | `interface PNEvent` |
| [PNMessageResult](-p-n-message-result/index.md) | Wrapper around an actual message received in [SubscribeCallback.message](../com.pubnub.api.callbacks/-subscribe-callback/message.md).`data class PNMessageResult : `[`MessageResult`](-message-result/index.md)`, `[`PubSubResult`](-pub-sub-result/index.md) |
| [PNPresenceEventResult](-p-n-presence-event-result/index.md) | Wrapper around a presence event received in [SubscribeCallback.presence](../com.pubnub.api.callbacks/-subscribe-callback/presence.md).`data class PNPresenceEventResult : `[`PNEvent`](-p-n-event.md) |
| [PNSignalResult](-p-n-signal-result/index.md) | Wrapper around a signal received in [SubscribeCallback.signal](../com.pubnub.api.callbacks/-subscribe-callback/signal.md).`data class PNSignalResult : `[`MessageResult`](-message-result/index.md)`, `[`PubSubResult`](-pub-sub-result/index.md) |
| [PubSubResult](-pub-sub-result/index.md) | `interface PubSubResult : `[`PNEvent`](-p-n-event.md) |
