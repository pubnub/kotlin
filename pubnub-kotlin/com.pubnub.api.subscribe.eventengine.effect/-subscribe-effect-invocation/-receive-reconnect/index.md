//[pubnub-kotlin](../../../../index.md)/[com.pubnub.api.subscribe.eventengine.effect](../../index.md)/[SubscribeEffectInvocation](../index.md)/[ReceiveReconnect](index.md)

# ReceiveReconnect

[jvm]\
data class [ReceiveReconnect](index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val subscriptionCursor: [SubscriptionCursor](../../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md), val attempts: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val reason: [PubNubException](../../../com.pubnub.api/-pub-nub-exception/index.md)?) : [SubscribeEffectInvocation](../index.md)

## Constructors

| | |
|---|---|
| [ReceiveReconnect](-receive-reconnect.md) | [jvm]<br>fun [ReceiveReconnect](-receive-reconnect.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, subscriptionCursor: [SubscriptionCursor](../../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md), attempts: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), reason: [PubNubException](../../../com.pubnub.api/-pub-nub-exception/index.md)?) |

## Properties

| Name | Summary |
|---|---|
| [attempts](attempts.md) | [jvm]<br>val [attempts](attempts.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [channelGroups](channel-groups.md) | [jvm]<br>val [channelGroups](channel-groups.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [channels](channels.md) | [jvm]<br>val [channels](channels.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [id](id.md) | [jvm]<br>open override val [id](id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [reason](reason.md) | [jvm]<br>val [reason](reason.md): [PubNubException](../../../com.pubnub.api/-pub-nub-exception/index.md)? |
| [subscriptionCursor](subscription-cursor.md) | [jvm]<br>val [subscriptionCursor](subscription-cursor.md): [SubscriptionCursor](../../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md) |
| [type](../type.md) | [jvm]<br>open override val [type](../type.md): [EffectInvocationType](../../../com.pubnub.api.eventengine/-effect-invocation-type/index.md) |
