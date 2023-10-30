//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.presence.eventengine.state](../index.md)/[PresenceState](index.md)

# PresenceState

[jvm]\
sealed class [PresenceState](index.md) : [State](../../com.pubnub.api.eventengine/-state/index.md)&lt;[PresenceEffectInvocation](../../com.pubnub.api.presence.eventengine.effect/-presence-effect-invocation/index.md), [PresenceEvent](../../com.pubnub.api.presence.eventengine.event/-presence-event/index.md), [PresenceState](index.md)&gt;

## Types

| Name | Summary |
|---|---|
| [HeartbeatCooldown](-heartbeat-cooldown/index.md) | [jvm]<br>data class [HeartbeatCooldown](-heartbeat-cooldown/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) : [PresenceState](index.md) |
| [HeartbeatFailed](-heartbeat-failed/index.md) | [jvm]<br>data class [HeartbeatFailed](-heartbeat-failed/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)?) : [PresenceState](index.md) |
| [HeartbeatInactive](-heartbeat-inactive/index.md) | [jvm]<br>object [HeartbeatInactive](-heartbeat-inactive/index.md) : [PresenceState](index.md) |
| [Heartbeating](-heartbeating/index.md) | [jvm]<br>data class [Heartbeating](-heartbeating/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) : [PresenceState](index.md) |
| [HeartbeatReconnecting](-heartbeat-reconnecting/index.md) | [jvm]<br>data class [HeartbeatReconnecting](-heartbeat-reconnecting/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val attempts: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)?) : [PresenceState](index.md) |
| [HeartbeatStopped](-heartbeat-stopped/index.md) | [jvm]<br>data class [HeartbeatStopped](-heartbeat-stopped/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) : [PresenceState](index.md) |

## Functions

| Name | Summary |
|---|---|
| [onEntry](../../com.pubnub.api.eventengine/-state/on-entry.md) | [jvm]<br>open fun [onEntry](../../com.pubnub.api.eventengine/-state/on-entry.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[PresenceEffectInvocation](../../com.pubnub.api.presence.eventengine.effect/-presence-effect-invocation/index.md)&gt; |
| [onExit](../../com.pubnub.api.eventengine/-state/on-exit.md) | [jvm]<br>open fun [onExit](../../com.pubnub.api.eventengine/-state/on-exit.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[PresenceEffectInvocation](../../com.pubnub.api.presence.eventengine.effect/-presence-effect-invocation/index.md)&gt; |
| [transition](index.md#192301925%2FFunctions%2F-1216412040) | [jvm]<br>abstract fun [transition](index.md#192301925%2FFunctions%2F-1216412040)(event: [PresenceEvent](../../com.pubnub.api.presence.eventengine.event/-presence-event/index.md)): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[PresenceState](index.md), [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[PresenceEffectInvocation](../../com.pubnub.api.presence.eventengine.effect/-presence-effect-invocation/index.md)&gt;&gt; |

## Inheritors

| Name |
|---|
| [HeartbeatInactive](-heartbeat-inactive/index.md) |
| [Heartbeating](-heartbeating/index.md) |
| [HeartbeatReconnecting](-heartbeat-reconnecting/index.md) |
| [HeartbeatStopped](-heartbeat-stopped/index.md) |
| [HeartbeatFailed](-heartbeat-failed/index.md) |
| [HeartbeatCooldown](-heartbeat-cooldown/index.md) |
