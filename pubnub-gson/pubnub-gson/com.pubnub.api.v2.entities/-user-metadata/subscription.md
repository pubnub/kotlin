//[pubnub-gson](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[UserMetadata](index.md)/[subscription](subscription.md)

# subscription

[jvm]\
abstract fun [subscription](subscription.md)(): [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md)

Returns a [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) that can be used to subscribe to this channel.

The returned [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) is initially inactive. You must call [Subscription.subscribe](../../com.pubnub.api.v2.subscriptions/-subscription/subscribe.md) on it to start receiving events.

#### Return

An inactive [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) to this channel.
