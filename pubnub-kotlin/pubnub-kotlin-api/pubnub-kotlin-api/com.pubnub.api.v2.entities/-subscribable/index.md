//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[Subscribable](index.md)

# Subscribable

interface [Subscribable](index.md)

This interface is implemented by entities that can be subscribed to, such as channels, channel groups, and user and channel metadata.

#### Inheritors

| |
|---|
| [Channel](../-channel/index.md) |
| [ChannelGroup](../-channel-group/index.md) |
| [ChannelMetadata](../-channel-metadata/index.md) |
| [UserMetadata](../-user-metadata/index.md) |

## Functions

| Name | Summary |
|---|---|
| [subscription](subscription.md) | [common]<br>abstract fun [subscription](subscription.md)(options: [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md) = EmptyOptions): [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md)<br>Returns a [com.pubnub.api.v2.subscriptions.Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) that can be used to subscribe to this `Subscribable`. |
