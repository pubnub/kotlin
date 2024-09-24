//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[SubscriptionSet](index.md)/[add](add.md)

# add

[common]\
abstract fun [add](add.md)(subscription: [Subscription](../-subscription/index.md))

Add a [Subscription](../-subscription/index.md) to this set.

Please note that this SubscriptionSet will *not* attempt to ensure all subscriptions match their active/inactive state. That is, if you previously called [subscribe](../../../../../pubnub-kotlin/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-set/subscribe.md) or [unsubscribe](../../../../../pubnub-kotlin/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-set/unsubscribe.md) on this set, it will not be called on the newly added [subscription](add.md) automatically.

#### Parameters

common

| | |
|---|---|
| subscription | the [Subscription](../-subscription/index.md) to add. |
