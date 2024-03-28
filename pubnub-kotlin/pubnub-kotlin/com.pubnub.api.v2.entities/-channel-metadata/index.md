//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[ChannelMetadata](index.md)

# ChannelMetadata

[jvm]\
interface [ChannelMetadata](index.md) : [BaseChannelMetadata](../../../../pubnub-gson/com.pubnub.api.v2.entities/-base-channel-metadata/index.md)&lt;[EventListener](../../com.pubnub.api.v2.callbacks/-event-listener/index.md), [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md)&gt; 

A representation of a PubNub entity for tracking channel metadata changes.

You can get a [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) to listen for metadata events through [Subscribable.subscription](../../../../pubnub-gson/com.pubnub.api.v2.entities/-subscribable/subscription.md).

Use the [com.pubnub.api.PubNub.channelMetadata](../../../../pubnub-gson/pubnub-gson/com.pubnub.api/-pub-nub/channel-metadata.md) factory method to create instances of this interface.

## Properties

| Name | Summary |
|---|---|
| [id](index.md#-1110608479%2FProperties%2F51989805) | [jvm]<br>abstract val [id](index.md#-1110608479%2FProperties%2F51989805): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Functions

| Name | Summary |
|---|---|
| [subscription](index.md#695905877%2FFunctions%2F51989805) | [jvm]<br>abstract override fun [subscription](index.md#695905877%2FFunctions%2F51989805)(options: [SubscriptionOptions](../../../../pubnub-gson/com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) |
