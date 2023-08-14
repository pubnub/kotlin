//[pubnub-kotlin](../../../../index.md)/[com.pubnub.api.subscribe.eventengine.state](../../index.md)/[SubscribeState](../index.md)/[Receiving](index.md)

# Receiving

[jvm]\
data class [Receiving](index.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, subscriptionCursor: [SubscriptionCursor](../../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)) : [SubscribeState](../index.md)

## Constructors

| | |
|---|---|
| [Receiving](-receiving.md) | [jvm]<br>fun [Receiving](-receiving.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, subscriptionCursor: [SubscriptionCursor](../../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [onEntry](on-entry.md) | [jvm]<br>open override fun [onEntry](on-entry.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation.ReceiveMessages](../../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/-receive-messages/index.md)&gt; |
| [onExit](on-exit.md) | [jvm]<br>open override fun [onExit](on-exit.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation.CancelReceiveMessages](../../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/-cancel-receive-messages/index.md)&gt; |
| [transition](transition.md) | [jvm]<br>open override fun [transition](transition.md)(event: [SubscribeEvent](../../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[SubscribeState](../index.md), [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation](../../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md)&gt;&gt; |
