//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[SubscriptionSet](index.md)/[plus](plus.md)

# plus

[jvm]\
abstract operator fun [plus](plus.md)(subscription: [Subscription](../-subscription/index.md)): [SubscriptionSet](index.md)

Adds a [Subscription](../-subscription/index.md) to this set. Equivalent to calling [add](add.md).

Please note that this [SubscriptionSet](index.md) will *not* attempt to ensure all subscriptions match their active/inactive state. That is, if you previously called subscribe or unsubscribe on this set, it will not be called on the newly added [subscription](plus.md) automatically.

## See also

jvm

| | |
|---|---|
| [com.pubnub.api.v2.subscriptions.SubscriptionSet](add.md) |  |

## Parameters

jvm

| | |
|---|---|
| subscription | the [Subscription](../-subscription/index.md) to add. |
