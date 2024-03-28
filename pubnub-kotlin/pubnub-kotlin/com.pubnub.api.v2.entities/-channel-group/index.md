//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[ChannelGroup](index.md)

# ChannelGroup

[jvm]\
interface [ChannelGroup](index.md) : [BaseChannelGroup](../../../../pubnub-gson/com.pubnub.api.v2.entities/-base-channel-group/index.md)&lt;[EventListener](../../com.pubnub.api.v2.callbacks/-event-listener/index.md), [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md)&gt; 

A representation of a PubNub channel group identified by its [name](../../../../pubnub-gson/com.pubnub.api.v2.entities/-channel-group/name.md).

You can get a [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) to this channel group through [Subscribable.subscription](../../../../pubnub-gson/com.pubnub.api.v2.entities/-subscribable/subscription.md).

Use the [com.pubnub.api.PubNub.channelGroup](../../../../pubnub-gson/pubnub-gson/com.pubnub.api/-pub-nub/channel-group.md) factory method to create instances of this interface.

## Properties

| Name | Summary |
|---|---|
| [name](index.md#-646709597%2FProperties%2F51989805) | [jvm]<br>abstract val [name](index.md#-646709597%2FProperties%2F51989805): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Functions

| Name | Summary |
|---|---|
| [subscription](index.md#-834878749%2FFunctions%2F51989805) | [jvm]<br>abstract override fun [subscription](index.md#-834878749%2FFunctions%2F51989805)(options: [SubscriptionOptions](../../../../pubnub-gson/com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) |
