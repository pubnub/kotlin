//[pubnub-core-api](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[BaseChannel](index.md)

# BaseChannel

[jvm]\
interface [BaseChannel](index.md)&lt;[EventListener](index.md) : [BaseEventListener](../../com.pubnub.api.v2.callbacks/-base-event-listener/index.md), [Subscription](index.md) : [BaseSubscription](../../com.pubnub.api.v2.subscriptions/-base-subscription/index.md)&lt;[EventListener](index.md)&gt;&gt; : [Subscribable](../-subscribable/index.md)&lt;[EventListener](index.md)&gt; 

A representation of a PubNub channel identified by its [name](name.md).

You can get a [Subscription](index.md) to this channel through [subscription](subscription.md).

Use the com.pubnub.api.PubNub.channel factory method to create instances of this interface.

## Properties

| Name | Summary |
|---|---|
| [name](name.md) | [jvm]<br>abstract val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name of this channel. Supports wildcards by ending it with &quot;.*&quot; |

## Functions

| Name | Summary |
|---|---|
| [subscription](subscription.md) | [jvm]<br>abstract override fun [subscription](subscription.md)(options: [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Subscription](index.md)<br>Returns a [Subscription](index.md) that can be used to subscribe to this channel. |
