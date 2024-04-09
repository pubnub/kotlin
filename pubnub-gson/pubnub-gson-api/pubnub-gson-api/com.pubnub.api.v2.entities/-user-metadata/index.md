//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[UserMetadata](index.md)

# UserMetadata

[jvm]\
interface [UserMetadata](index.md) : [BaseUserMetadata](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.entities/-base-user-metadata/index.md)&lt;[EventListener](../../com.pubnub.api.v2.callbacks/-event-listener/index.md), [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md)&gt; 

A representation of a PubNub entity for tracking user metadata changes.

You can get a [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) to listen for metadata events through [Subscribable.subscription](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.entities/-subscribable/subscription.md).

Use the [com.pubnub.api.PubNub.userMetadata](../../com.pubnub.api/-pub-nub/user-metadata.md) factory method to create instances of this interface.

## Properties

| Name | Summary |
|---|---|
| [id](index.md#-195509711%2FProperties%2F126356644) | [jvm]<br>abstract val [id](index.md#-195509711%2FProperties%2F126356644): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Functions

| Name | Summary |
|---|---|
| [subscription](index.md#991013829%2FFunctions%2F126356644) | [jvm]<br>abstract override fun [subscription](index.md#991013829%2FFunctions%2F126356644)(options: [SubscriptionOptions](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md)<br>[jvm]<br>abstract fun [subscription](subscription.md)(): [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md)<br>Returns a [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) that can be used to subscribe to this channel. |
