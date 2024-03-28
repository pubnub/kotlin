//[pubnub-core-api](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[Subscribable](index.md)/[subscription](subscription.md)

# subscription

[jvm]\
abstract fun [subscription](subscription.md)(options: [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md) = EmptyOptions): [BaseSubscription](../../com.pubnub.api.v2.subscriptions/-base-subscription/index.md)&lt;[EvLis](index.md)&gt;

Returns a com.pubnub.api.v2.subscriptions.Subscription that can be used to subscribe to this `Subscribable`.

#### Return

an inactive Subscription to this `Subscribable`. You must call Subscription.subscribe to start receiving events.

#### Parameters

jvm

| | |
|---|---|
| options | optional [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md). |
