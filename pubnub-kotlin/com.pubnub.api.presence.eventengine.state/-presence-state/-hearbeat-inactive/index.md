//[pubnub-kotlin](../../../../index.md)/[com.pubnub.api.presence.eventengine.state](../../index.md)/[PresenceState](../index.md)/[HearbeatInactive](index.md)

# HearbeatInactive

[jvm]\
object [HearbeatInactive](index.md) : [PresenceState](../index.md)

## Functions

| Name | Summary |
|---|---|
| [onEntry](../../../com.pubnub.api.eventengine/-state/on-entry.md) | [jvm]<br>open fun [onEntry](../../../com.pubnub.api.eventengine/-state/on-entry.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[PresenceEffectInvocation](../../../com.pubnub.api.presence.eventengine.effect/-presence-effect-invocation/index.md)&gt; |
| [onExit](../../../com.pubnub.api.eventengine/-state/on-exit.md) | [jvm]<br>open fun [onExit](../../../com.pubnub.api.eventengine/-state/on-exit.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[PresenceEffectInvocation](../../../com.pubnub.api.presence.eventengine.effect/-presence-effect-invocation/index.md)&gt; |
| [transition](transition.md) | [jvm]<br>open override fun [transition](transition.md)(event: [PresenceEvent](../../../com.pubnub.api.presence.eventengine.event/-presence-event/index.md)): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[PresenceState](../index.md), [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[PresenceEffectInvocation](../../../com.pubnub.api.presence.eventengine.effect/-presence-effect-invocation/index.md)&gt;&gt; |
