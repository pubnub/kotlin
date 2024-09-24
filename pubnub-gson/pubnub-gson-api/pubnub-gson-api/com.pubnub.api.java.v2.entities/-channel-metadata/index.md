//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.entities](../index.md)/[ChannelMetadata](index.md)

# ChannelMetadata

[jvm]\
interface [ChannelMetadata](index.md) : [Subscribable](../-subscribable/index.md)

A representation of a PubNub entity for tracking channel metadata changes.

You can get a [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md) to listen for metadata events through [Subscribable.subscription](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.entities/-subscribable/subscription.md).

Use the [com.pubnub.api.java.PubNub.channelMetadata](../../com.pubnub.api.java/-pub-nub/channel-metadata.md) factory method to create instances of this interface.

## Properties

| Name | Summary |
|---|---|
| [id](id.md) | [jvm]<br>abstract val [id](id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The id for this channel metadata object. |

## Functions

| Name | Summary |
|---|---|
| [subscription](subscription.md) | [jvm]<br>abstract fun [subscription](subscription.md)(): [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md)<br>Returns a [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md) that can be used to subscribe to this channel.<br>[jvm]<br>abstract override fun [subscription](subscription.md)(options: [SubscriptionOptions](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md)<br>Returns a [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md) that can be used to subscribe to this channel metadata. |
