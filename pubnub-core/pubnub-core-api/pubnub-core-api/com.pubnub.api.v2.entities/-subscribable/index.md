//[pubnub-core-api](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[Subscribable](index.md)

# Subscribable

interface [Subscribable](index.md)&lt;[EvLis](index.md) : [BaseEventListener](../../com.pubnub.api.v2.callbacks/-base-event-listener/index.md)&gt;

This interface is implemented by entities that can be subscribed to, such as channels, channel groups, and user and channel metadata.

#### Inheritors

| |
|---|
| [BaseChannel](../-base-channel/index.md) |
| [BaseChannelGroup](../-base-channel-group/index.md) |
| [BaseChannelMetadata](../-base-channel-metadata/index.md) |
| [BaseUserMetadata](../-base-user-metadata/index.md) |

## Functions

| Name | Summary |
|---|---|
| [subscription](subscription.md) | [jvm]<br>abstract fun [subscription](subscription.md)(options: [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md) = EmptyOptions): [BaseSubscription](../../com.pubnub.api.v2.subscriptions/-base-subscription/index.md)&lt;[EvLis](index.md)&gt;<br>Returns a com.pubnub.api.v2.subscriptions.Subscription that can be used to subscribe to this `Subscribable`. |
