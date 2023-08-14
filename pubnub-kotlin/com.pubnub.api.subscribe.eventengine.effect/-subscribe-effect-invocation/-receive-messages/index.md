//[pubnub-kotlin](../../../../index.md)/[com.pubnub.api.subscribe.eventengine.effect](../../index.md)/[SubscribeEffectInvocation](../index.md)/[ReceiveMessages](index.md)

# ReceiveMessages

[jvm]\
data class [ReceiveMessages](index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val subscriptionCursor: [SubscriptionCursor](../../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)) : [SubscribeEffectInvocation](../index.md)

## Constructors

| | |
|---|---|
| [ReceiveMessages](-receive-messages.md) | [jvm]<br>fun [ReceiveMessages](-receive-messages.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, subscriptionCursor: [SubscriptionCursor](../../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [channelGroups](channel-groups.md) | [jvm]<br>val [channelGroups](channel-groups.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [channels](channels.md) | [jvm]<br>val [channels](channels.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [id](id.md) | [jvm]<br>open override val [id](id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [subscriptionCursor](subscription-cursor.md) | [jvm]<br>val [subscriptionCursor](subscription-cursor.md): [SubscriptionCursor](../../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md) |
| [type](../type.md) | [jvm]<br>open override val [type](../type.md): [EffectInvocationType](../../../com.pubnub.api.eventengine/-effect-invocation-type/index.md) |
