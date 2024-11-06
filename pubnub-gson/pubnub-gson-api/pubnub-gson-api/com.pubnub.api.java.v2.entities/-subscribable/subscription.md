//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.entities](../index.md)/[Subscribable](index.md)/[subscription](subscription.md)

# subscription

[jvm]\
abstract fun [subscription](subscription.md)(options: [SubscriptionOptions](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.subscriptions/-subscription-options/index.md) = EmptyOptions): [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md)

Returns a com.pubnub.api.v2.subscriptions.Subscription that can be used to subscribe to this `Subscribable`.

#### Return

an inactive Subscription to this `Subscribable`. You must call Subscription.subscribe to start receiving events.

#### Parameters

jvm

| | |
|---|---|
| options | optional [SubscriptionOptions](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.subscriptions/-subscription-options/index.md). |
