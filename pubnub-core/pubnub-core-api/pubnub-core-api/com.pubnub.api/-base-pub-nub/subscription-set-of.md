//[pubnub-core-api](../../../index.md)/[com.pubnub.api](../index.md)/[BasePubNub](index.md)/[subscriptionSetOf](subscription-set-of.md)

# subscriptionSetOf

[jvm]\
abstract fun [subscriptionSetOf](subscription-set-of.md)(subscriptions: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Subscription](index.md)&gt;): [SubscriptionSet](index.md)

Create a [SubscriptionSet](index.md) from the given [subscriptions](subscription-set-of.md).

#### Return

a [SubscriptionSet](index.md) containing all [subscriptions](subscription-set-of.md)

#### Parameters

jvm

| | |
|---|---|
| subscriptions | the subscriptions that will be added to the returned [SubscriptionSet](index.md) |

[jvm]\
abstract fun [subscriptionSetOf](subscription-set-of.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptySet(), channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptySet(), options: [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md) = EmptyOptions): [SubscriptionSet](index.md)

Create a [SubscriptionSet](index.md) containing [Subscription](index.md) objects for the given sets of [channels](subscription-set-of.md) and [channelGroups](subscription-set-of.md).

Please note that the subscriptions are not active until you call SubscriptionSet.subscribe.

This is a convenience method, and it is equal to calling PubNub.channel followed by Channel.subscription for each channel, then creating a [subscriptionSetOf](subscription-set-of.md) using the returned [Subscription](index.md) objects (and similarly for channel groups).

#### Return

a [SubscriptionSet](index.md) containing subscriptions for the given [channels](subscription-set-of.md) and [channelGroups](subscription-set-of.md)

#### Parameters

jvm

| | |
|---|---|
| channels | the channels to create subscriptions for |
| channelGroups | the channel groups to create subscriptions for |
| options | the [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md) to pass for each subscription. Refer to supported options in [Channel](index.md) and [ChannelGroup](index.md) documentation. |
