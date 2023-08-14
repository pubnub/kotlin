//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.presence.eventengine.event](../index.md)/[PresenceEvent](index.md)

# PresenceEvent

[jvm]\
sealed class [PresenceEvent](index.md) : [Event](../../com.pubnub.api.eventengine/-event/index.md)

## Types

| Name | Summary |
|---|---|
| [Disconnect](-disconnect/index.md) | [jvm]<br>object [Disconnect](-disconnect/index.md) : [PresenceEvent](index.md) |
| [HeartbeatFailure](-heartbeat-failure/index.md) | [jvm]<br>data class [HeartbeatFailure](-heartbeat-failure/index.md)(val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)) : [PresenceEvent](index.md) |
| [HeartbeatGiveup](-heartbeat-giveup/index.md) | [jvm]<br>data class [HeartbeatGiveup](-heartbeat-giveup/index.md)(val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)) : [PresenceEvent](index.md) |
| [HeartbeatSuccess](-heartbeat-success/index.md) | [jvm]<br>object [HeartbeatSuccess](-heartbeat-success/index.md) : [PresenceEvent](index.md) |
| [Joined](-joined/index.md) | [jvm]<br>data class [Joined](-joined/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) : [PresenceEvent](index.md) |
| [Left](-left/index.md) | [jvm]<br>data class [Left](-left/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) : [PresenceEvent](index.md) |
| [LeftAll](-left-all/index.md) | [jvm]<br>object [LeftAll](-left-all/index.md) : [PresenceEvent](index.md) |
| [Reconnect](-reconnect/index.md) | [jvm]<br>object [Reconnect](-reconnect/index.md) : [PresenceEvent](index.md) |
| [StateSet](-state-set/index.md) | [jvm]<br>data class [StateSet](-state-set/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) : [PresenceEvent](index.md) |
| [TimesUp](-times-up/index.md) | [jvm]<br>object [TimesUp](-times-up/index.md) : [PresenceEvent](index.md) |

## Inheritors

| Name |
|---|
| [Joined](-joined/index.md) |
| [Left](-left/index.md) |
| [Reconnect](-reconnect/index.md) |
| [Disconnect](-disconnect/index.md) |
| [LeftAll](-left-all/index.md) |
| [TimesUp](-times-up/index.md) |
| [HeartbeatSuccess](-heartbeat-success/index.md) |
| [HeartbeatFailure](-heartbeat-failure/index.md) |
| [HeartbeatGiveup](-heartbeat-giveup/index.md) |
| [StateSet](-state-set/index.md) |
