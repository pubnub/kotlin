//[pubnub-kotlin](../../../../index.md)/[com.pubnub.api.subscribe.eventengine.event](../../index.md)/[SubscribeEvent](../index.md)/[SubscriptionRestored](index.md)

# SubscriptionRestored

[jvm]\
data class [SubscriptionRestored](index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val subscriptionCursor: [SubscriptionCursor](../../-subscription-cursor/index.md)) : [SubscribeEvent](../index.md)

## Constructors

| | |
|---|---|
| [SubscriptionRestored](-subscription-restored.md) | [jvm]<br>fun [SubscriptionRestored](-subscription-restored.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, subscriptionCursor: [SubscriptionCursor](../../-subscription-cursor/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [channelGroups](channel-groups.md) | [jvm]<br>val [channelGroups](channel-groups.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [channels](channels.md) | [jvm]<br>val [channels](channels.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [subscriptionCursor](subscription-cursor.md) | [jvm]<br>val [subscriptionCursor](subscription-cursor.md): [SubscriptionCursor](../../-subscription-cursor/index.md) |
