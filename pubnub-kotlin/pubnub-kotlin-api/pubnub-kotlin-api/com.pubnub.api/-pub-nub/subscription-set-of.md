//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[subscriptionSetOf](subscription-set-of.md)

# subscriptionSetOf

[common]\
expect abstract fun [subscriptionSetOf](subscription-set-of.md)(subscriptions: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md)&gt;): [SubscriptionSet](../../com.pubnub.api.v2.subscriptions/-subscription-set/index.md)actual abstract fun [subscriptionSetOf](subscription-set-of.md)(subscriptions: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md)&gt;): [SubscriptionSet](../../com.pubnub.api.v2.subscriptions/-subscription-set/index.md)

expect abstract fun [subscriptionSetOf](subscription-set-of.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptySet(), channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptySet(), options: [SubscriptionOptions](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.subscriptions/-subscription-options/index.md) = EmptyOptions): [SubscriptionSet](../../com.pubnub.api.v2.subscriptions/-subscription-set/index.md)actual abstract fun [subscriptionSetOf](subscription-set-of.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, options: [SubscriptionOptions](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [SubscriptionSet](../../com.pubnub.api.v2.subscriptions/-subscription-set/index.md)

[jvm]\
actual abstract fun [subscriptionSetOf](subscription-set-of.md)(subscriptions: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Subscription](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription/index.md)&gt;): [SubscriptionSet](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-set/index.md)

Create a [SubscriptionSet](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-set/index.md) from the given [subscriptions](subscription-set-of.md).

#### Return

a [SubscriptionSet](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-set/index.md) containing all [subscriptions](subscription-set-of.md)

#### Parameters

jvm

| | |
|---|---|
| subscriptions | the subscriptions that will be added to the returned [SubscriptionSet](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-set/index.md) |

[jvm]\
actual abstract fun [subscriptionSetOf](subscription-set-of.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, options: [SubscriptionOptions](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [SubscriptionSet](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-set/index.md)

Create a [SubscriptionSet](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-set/index.md) containing [Subscription](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription/index.md) objects for the given sets of [channels](subscription-set-of.md) and [channelGroups](subscription-set-of.md).

Please note that the subscriptions are not active until you call SubscriptionSet.subscribe.

This is a convenience method, and it is equal to calling [PubNub.channel](channel.md) followed by [Channel.subscription](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.entities/-channel/subscription.md) for each channel, then creating a [subscriptionSetOf](subscription-set-of.md) using the returned [Subscription](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription/index.md) objects (and similarly for channel groups).

#### Return

a [SubscriptionSet](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-set/index.md) containing subscriptions for the given [channels](subscription-set-of.md) and [channelGroups](subscription-set-of.md)

#### Parameters

jvm

| | |
|---|---|
| channels | the channels to create subscriptions for |
| channelGroups | the channel groups to create subscriptions for |
| options | the [SubscriptionOptions](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.subscriptions/-subscription-options/index.md) to pass for each subscription. Refer to supported options in [Channel](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.entities/-channel/index.md) and [ChannelGroup](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.entities/-channel-group/index.md) documentation. |
