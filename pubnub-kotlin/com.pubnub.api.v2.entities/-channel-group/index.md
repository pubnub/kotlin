//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[ChannelGroup](index.md)

# ChannelGroup

[jvm]\
interface [ChannelGroup](index.md) : [Subscribable](../-subscribable/index.md)

A representation of a PubNub channel group identified by its [name](name.md).

You can get a [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) to this channel group through [subscription](subscription.md).

Use the [com.pubnub.api.PubNub.channelGroup](../../com.pubnub.api/-pub-nub/channel-group.md) factory method to create instances of this interface.

## Functions

| Name | Summary |
|---|---|
| [subscription](subscription.md) | [jvm]<br>abstract override fun [subscription](subscription.md)(options: [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md)<br>Returns a [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) that can be used to subscribe to this channel group. |

## Properties

| Name | Summary |
|---|---|
| [name](name.md) | [jvm]<br>abstract val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name of this channel group. |
