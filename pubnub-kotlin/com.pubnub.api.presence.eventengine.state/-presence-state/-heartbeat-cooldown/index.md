//[pubnub-kotlin](../../../../index.md)/[com.pubnub.api.presence.eventengine.state](../../index.md)/[PresenceState](../index.md)/[HeartbeatCooldown](index.md)

# HeartbeatCooldown

[jvm]\
data class [HeartbeatCooldown](index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) : [PresenceState](../index.md)

## Constructors

| | |
|---|---|
| [HeartbeatCooldown](-heartbeat-cooldown.md) | [jvm]<br>fun [HeartbeatCooldown](-heartbeat-cooldown.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) |

## Functions

| Name | Summary |
|---|---|
| [onEntry](on-entry.md) | [jvm]<br>open override fun [onEntry](on-entry.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[PresenceEffectInvocation](../../../com.pubnub.api.presence.eventengine.effect/-presence-effect-invocation/index.md)&gt; |
| [onExit](on-exit.md) | [jvm]<br>open override fun [onExit](on-exit.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[PresenceEffectInvocation](../../../com.pubnub.api.presence.eventengine.effect/-presence-effect-invocation/index.md)&gt; |
| [transition](transition.md) | [jvm]<br>open override fun [transition](transition.md)(event: [PresenceEvent](../../../com.pubnub.api.presence.eventengine.event/-presence-event/index.md)): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[PresenceState](../index.md), [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[PresenceEffectInvocation](../../../com.pubnub.api.presence.eventengine.effect/-presence-effect-invocation/index.md)&gt;&gt; |

## Properties

| Name | Summary |
|---|---|
| [channelGroups](channel-groups.md) | [jvm]<br>val [channelGroups](channel-groups.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [channels](channels.md) | [jvm]<br>val [channels](channels.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
