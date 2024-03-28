//[pubnub-core-api](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[BaseSubscriptionSet](index.md)/[add](add.md)

# add

[jvm]\
abstract fun [add](add.md)(subscription: [Subscription](index.md))

Add a [Subscription](index.md) to this set.

Please note that this SubscriptionSet will *not* attempt to ensure all subscriptions match their active/inactive state. That is, if you previously called [subscribe](../../../../../pubnub-gson/com.pubnub.api.v2.subscriptions/-base-subscription-set/subscribe.md) or [unsubscribe](../../../../../pubnub-gson/com.pubnub.api.v2.subscriptions/-base-subscription-set/unsubscribe.md) on this set, it will not be called on the newly added [subscription](add.md) automatically.

#### Parameters

jvm

| | |
|---|---|
| subscription | the [Subscription](index.md) to add. |
