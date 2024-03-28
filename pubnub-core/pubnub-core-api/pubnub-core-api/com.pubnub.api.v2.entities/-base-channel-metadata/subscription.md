//[pubnub-core-api](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[BaseChannelMetadata](index.md)/[subscription](subscription.md)

# subscription

[jvm]\
abstract override fun [subscription](subscription.md)(options: [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Sub](index.md)

Returns a Subscription that can be used to subscribe to this channel metadata.

[com.pubnub.api.v2.subscriptions.SubscriptionOptions.filter](../../com.pubnub.api.v2.subscriptions/-subscription-options/-companion/filter.md) can be used to filter events delivered to the subscription.

#### Return

an inactive Subscription to this channel metadata. You must call Subscription.subscribe to start receiving events.

#### Parameters

jvm

| | |
|---|---|
| options | optional [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md). |
