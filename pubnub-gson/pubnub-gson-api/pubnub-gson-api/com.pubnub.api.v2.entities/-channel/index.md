//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[Channel](index.md)

# Channel

[jvm]\
interface [Channel](index.md) : [BaseChannel](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.entities/-base-channel/index.md)&lt;[EventListener](../../com.pubnub.api.v2.callbacks/-event-listener/index.md), [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md)&gt; 

A representation of a PubNub channel identified by its [name](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.v2.entities/-channel/name.md).

You can get a [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) to this channel through [Subscribable.subscription](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.entities/-subscribable/subscription.md).

Use the [com.pubnub.api.PubNub.channel](../../com.pubnub.api/-pub-nub/channel.md) factory method to create instances of this interface.

## Properties

| Name | Summary |
|---|---|
| [name](index.md#-1931444638%2FProperties%2F126356644) | [jvm]<br>abstract val [name](index.md#-1931444638%2FProperties%2F126356644): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Functions

| Name | Summary |
|---|---|
| [fire](fire.md) | [jvm]<br>abstract fun [fire](fire.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [PublishBuilder](../../com.pubnub.api.v2.endpoints.pubsub/-publish-builder/index.md)<br>Send a message to PubNub Functions Event Handlers. |
| [publish](publish.md) | [jvm]<br>abstract fun [publish](publish.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [PublishBuilder](../../com.pubnub.api.v2.endpoints.pubsub/-publish-builder/index.md)<br>Send a message to all subscribers of the channel. |
| [signal](signal.md) | [jvm]<br>abstract fun [signal](signal.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [SignalBuilder](../../com.pubnub.api.v2.endpoints.pubsub/-signal-builder/index.md)<br>Send a signal to all subscribers of a channel. |
| [subscription](index.md#1711906756%2FFunctions%2F126356644) | [jvm]<br>abstract override fun [subscription](index.md#1711906756%2FFunctions%2F126356644)(options: [SubscriptionOptions](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md)<br>[jvm]<br>abstract fun [subscription](subscription.md)(): [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md)<br>Returns a [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) that can be used to subscribe to this channel. |
