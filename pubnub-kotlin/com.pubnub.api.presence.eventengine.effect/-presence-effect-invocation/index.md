//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.presence.eventengine.effect](../index.md)/[PresenceEffectInvocation](index.md)

# PresenceEffectInvocation

[jvm]\
sealed class [PresenceEffectInvocation](index.md) : [EffectInvocation](../../com.pubnub.api.eventengine/-effect-invocation/index.md)

## Types

| Name | Summary |
|---|---|
| [CancelDelayedHeartbeat](-cancel-delayed-heartbeat/index.md) | [jvm]<br>object [CancelDelayedHeartbeat](-cancel-delayed-heartbeat/index.md) : [PresenceEffectInvocation](index.md) |
| [CancelWait](-cancel-wait/index.md) | [jvm]<br>object [CancelWait](-cancel-wait/index.md) : [PresenceEffectInvocation](index.md) |
| [DelayedHeartbeat](-delayed-heartbeat/index.md) | [jvm]<br>class [DelayedHeartbeat](-delayed-heartbeat/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val attempts: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)?) : [PresenceEffectInvocation](index.md) |
| [Heartbeat](-heartbeat/index.md) | [jvm]<br>data class [Heartbeat](-heartbeat/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) : [PresenceEffectInvocation](index.md) |
| [Leave](-leave/index.md) | [jvm]<br>data class [Leave](-leave/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) : [PresenceEffectInvocation](index.md) |
| [Wait](-wait/index.md) | [jvm]<br>class [Wait](-wait/index.md) : [PresenceEffectInvocation](index.md) |

## Properties

| Name | Summary |
|---|---|
| [id](id.md) | [jvm]<br>open override val [id](id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [type](type.md) | [jvm]<br>open override val [type](type.md): [EffectInvocationType](../../com.pubnub.api.eventengine/-effect-invocation-type/index.md) |

## Inheritors

| Name |
|---|
| [Heartbeat](-heartbeat/index.md) |
| [Leave](-leave/index.md) |
| [Wait](-wait/index.md) |
| [CancelWait](-cancel-wait/index.md) |
| [DelayedHeartbeat](-delayed-heartbeat/index.md) |
| [CancelDelayedHeartbeat](-cancel-delayed-heartbeat/index.md) |
