//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.entities](../index.md)/[Channel](index.md)

# Channel

[jvm]\
interface [Channel](index.md) : [Subscribable](../-subscribable/index.md)

A representation of a PubNub channel identified by its [name](name.md).

You can get a [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md) to this channel through [Subscribable.subscription](../-subscribable/subscription.md).

Use the [com.pubnub.api.java.PubNub.channel](../../com.pubnub.api.java/-pub-nub/channel.md) factory method to create instances of this interface.

## Properties

| Name | Summary |
|---|---|
| [name](name.md) | [jvm]<br>abstract val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name of this channel. Supports wildcards by ending it with &quot;.*&quot; |

## Functions

| Name | Summary |
|---|---|
| [fire](fire.md) | [jvm]<br>abstract fun [fire](fire.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [PublishBuilder](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md)<br>Send a message to PubNub Functions Event Handlers. |
| [publish](publish.md) | [jvm]<br>abstract fun [publish](publish.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [PublishBuilder](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md)<br>Send a message to all subscribers of the channel. |
| [signal](signal.md) | [jvm]<br>abstract fun [signal](signal.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Signal](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.pubsub/-signal/index.md)<br>Send a signal to all subscribers of a channel. |
| [subscription](subscription.md) | [jvm]<br>abstract fun [subscription](subscription.md)(): [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md)<br>abstract override fun [subscription](subscription.md)(options: [SubscriptionOptions](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md)<br>Returns a [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md) that can be used to subscribe to this channel. |
