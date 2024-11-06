//[pubnub-kotlin-core-api](../../../../index.md)/[com.pubnub.api.v2.subscriptions](../../index.md)/[SubscriptionOptions](../index.md)/[Companion](index.md)/[filter](filter.md)

# filter

[common]\

@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-static/index.html)

fun [filter](filter.md)(predicate: ([PNEvent](../../../com.pubnub.api.models.consumer.pubsub/-p-n-event/index.md)) -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [SubscriptionOptions](../index.md)

Create a filter for messages delivered to com.pubnub.api.v2.callbacks.BaseEventListener. Please see com.pubnub.api.v2.callbacks.BaseEventListener for available events.
