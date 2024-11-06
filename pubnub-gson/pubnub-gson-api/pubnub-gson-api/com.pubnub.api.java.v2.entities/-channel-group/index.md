//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.entities](../index.md)/[ChannelGroup](index.md)

# ChannelGroup

[jvm]\
interface [ChannelGroup](index.md) : [Subscribable](../-subscribable/index.md)

A representation of a PubNub channel group identified by its [name](name.md).

You can get a [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md) to this channel group through [Subscribable.subscription](../-subscribable/subscription.md).

Use the [com.pubnub.api.java.PubNub.channelGroup](../../com.pubnub.api.java/-pub-nub/channel-group.md) factory method to create instances of this interface.

## Properties

| Name | Summary |
|---|---|
| [name](name.md) | [jvm]<br>abstract val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name of this channel group. |

## Functions

| Name | Summary |
|---|---|
| [subscription](subscription.md) | [jvm]<br>abstract fun [subscription](subscription.md)(): [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md)<br>abstract override fun [subscription](subscription.md)(options: [SubscriptionOptions](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md)<br>Returns a [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md) that can be used to subscribe to this channel group. |
