//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[Channel](index.md)

# Channel

[jvm]\
interface [Channel](index.md) : [BaseChannel](../../../../pubnub-gson/com.pubnub.api.v2.entities/-base-channel/index.md)&lt;[EventListener](../../com.pubnub.api.v2.callbacks/-event-listener/index.md), [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md)&gt; 

A representation of a PubNub channel identified by its [name](../../../../pubnub-gson/com.pubnub.api.v2.entities/-channel/name.md).

You can get a [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) to this channel through [Subscribable.subscription](../../../../pubnub-gson/com.pubnub.api.v2.entities/-subscribable/subscription.md).

Use the [com.pubnub.api.PubNub.channel](../../../../pubnub-gson/pubnub-gson/com.pubnub.api/-pub-nub/channel.md) factory method to create instances of this interface.

## Properties

| Name | Summary |
|---|---|
| [name](index.md#-1931444638%2FProperties%2F51989805) | [jvm]<br>abstract val [name](index.md#-1931444638%2FProperties%2F51989805): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Functions

| Name | Summary |
|---|---|
| [subscription](index.md#1711906756%2FFunctions%2F51989805) | [jvm]<br>abstract override fun [subscription](index.md#1711906756%2FFunctions%2F51989805)(options: [SubscriptionOptions](../../../../pubnub-gson/com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) |
