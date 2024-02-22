//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[Channel](index.md)

# Channel

[jvm]\
interface [Channel](index.md) : [Subscribable](../-subscribable/index.md)

A representation of a PubNub channel identified by its [name](name.md).

You can get a [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) to this channel through [subscription](subscription.md).

Use the [com.pubnub.api.PubNub.channel](../../com.pubnub.api/-pub-nub/channel.md) factory method to create instances of this interface.

## Functions

| Name | Summary |
|---|---|
| [subscription](subscription.md) | [jvm]<br>abstract override fun [subscription](subscription.md)(options: [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md)<br>Returns a [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) that can be used to subscribe to this channel. |

## Properties

| Name | Summary |
|---|---|
| [name](name.md) | [jvm]<br>abstract val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name of this channel. Supports wildcards by ending it with &quot;.*&quot; |
