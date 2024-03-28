//[pubnub-core-api](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[BaseUserMetadata](index.md)

# BaseUserMetadata

[jvm]\
interface [BaseUserMetadata](index.md)&lt;[Lis](index.md) : [BaseEventListener](../../com.pubnub.api.v2.callbacks/-base-event-listener/index.md), [Sub](index.md) : [BaseSubscription](../../com.pubnub.api.v2.subscriptions/-base-subscription/index.md)&lt;[Lis](index.md)&gt;&gt; : [Subscribable](../-subscribable/index.md)&lt;[Lis](index.md)&gt;

## Properties

| Name | Summary |
|---|---|
| [id](id.md) | [jvm]<br>abstract val [id](id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The id for this user metadata object. |

## Functions

| Name | Summary |
|---|---|
| [subscription](subscription.md) | [jvm]<br>abstract override fun [subscription](subscription.md)(options: [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Sub](index.md)<br>Returns a Subscription that can be used to subscribe to this user metadata. |
