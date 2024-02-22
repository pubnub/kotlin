//[pubnub-kotlin](../../../../index.md)/[com.pubnub.api.v2.subscriptions](../../index.md)/[SubscriptionOptions](../index.md)/[Companion](index.md)/[filter](filter.md)

# filter

[jvm]\

@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-static/index.html)

fun [filter](filter.md)(predicate: ([PNEvent](../../../com.pubnub.api.models.consumer.pubsub/-p-n-event/index.md)) -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [SubscriptionOptions](../index.md)

Create a filter for messages delivered to [com.pubnub.api.v2.callbacks.EventListener](../../../com.pubnub.api.v2.callbacks/-event-listener/index.md). Please see [com.pubnub.api.v2.callbacks.EventListener](../../../com.pubnub.api.v2.callbacks/-event-listener/index.md) for available events.
