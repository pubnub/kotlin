//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[SubscriptionSet](index.md)/[add](add.md)

# add

[jvm]\
abstract fun [add](add.md)(subscription: [Subscription](../-subscription/index.md))

Add a [Subscription](../-subscription/index.md) to this set.

Please note that this [SubscriptionSet](index.md) will *not* attempt to ensure all subscriptions match their active/inactive state. That is, if you previously called subscribe or unsubscribe on this set, it will not be called on the newly added [subscription](add.md) automatically.

## See also

jvm

| | |
|---|---|
| [com.pubnub.api.v2.subscriptions.SubscriptionSet](plus.md) |  |

## Parameters

jvm

| | |
|---|---|
| subscription | the [Subscription](../-subscription/index.md) to add. |
