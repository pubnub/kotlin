//[pubnub-core-api](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[BaseSubscriptionSet](index.md)/[remove](remove.md)

# remove

[jvm]\
abstract fun [remove](remove.md)(subscription: [Subscription](index.md))

Remove the [subscription](remove.md) from this set.

Please note that removing a subscription from the set does not automatically [unsubscribe](../../../../../pubnub-gson/com.pubnub.api.v2.subscriptions/-base-subscription-set/unsubscribe.md) or [close](../../../../../pubnub-gson/com.pubnub.api.v2.subscriptions/-base-subscription-set/close.md) it.

#### Parameters

jvm

| | |
|---|---|
| subscription | the [Subscription](index.md) to remove. |
