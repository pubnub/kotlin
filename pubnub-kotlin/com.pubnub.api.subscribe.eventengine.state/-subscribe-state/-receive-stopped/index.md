//[pubnub-kotlin](../../../../index.md)/[com.pubnub.api.subscribe.eventengine.state](../../index.md)/[SubscribeState](../index.md)/[ReceiveStopped](index.md)

# ReceiveStopped

[jvm]\
data class [ReceiveStopped](index.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val subscriptionCursor: [SubscriptionCursor](../../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)) : [SubscribeState](../index.md)

## Constructors

| | |
|---|---|
| [ReceiveStopped](-receive-stopped.md) | [jvm]<br>fun [ReceiveStopped](-receive-stopped.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, subscriptionCursor: [SubscriptionCursor](../../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [onEntry](../../../com.pubnub.api.eventengine/-state/on-entry.md) | [jvm]<br>open fun [onEntry](../../../com.pubnub.api.eventengine/-state/on-entry.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation](../../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md)&gt; |
| [onExit](../../../com.pubnub.api.eventengine/-state/on-exit.md) | [jvm]<br>open fun [onExit](../../../com.pubnub.api.eventengine/-state/on-exit.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation](../../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md)&gt; |
| [transition](transition.md) | [jvm]<br>open override fun [transition](transition.md)(event: [SubscribeEvent](../../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[SubscribeState](../index.md), [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation](../../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md)&gt;&gt; |

## Properties

| Name | Summary |
|---|---|
| [channelGroups](channel-groups.md) | [jvm]<br>val [channelGroups](channel-groups.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [subscriptionCursor](subscription-cursor.md) | [jvm]<br>val [subscriptionCursor](subscription-cursor.md): [SubscriptionCursor](../../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md) |
