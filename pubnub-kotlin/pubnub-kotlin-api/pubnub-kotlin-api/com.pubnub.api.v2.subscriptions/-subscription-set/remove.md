//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[SubscriptionSet](index.md)/[remove](remove.md)

# remove

[common]\
abstract fun [remove](remove.md)(subscription: [Subscription](../-subscription/index.md))

Remove the [subscription](remove.md) from this set.

Please note that removing a subscription from the set does not automatically [unsubscribe](../../../../../pubnub-kotlin/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-set/unsubscribe.md) or [close](../../../../../pubnub-kotlin/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-set/close.md) it.

#### Parameters

common

| | |
|---|---|
| subscription | the [Subscription](../-subscription/index.md) to remove. |
