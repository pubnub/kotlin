//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.eventengine](../index.md)/[State](index.md)

# State

[jvm]\
interface [State](index.md)&lt;[Ei](index.md) : [EffectInvocation](../-effect-invocation/index.md), [Ev](index.md) : [Event](../-event/index.md), [S](index.md) : [State](index.md)&lt;[Ei](index.md), [Ev](index.md), [S](index.md)&gt;&gt;

## Functions

| Name | Summary |
|---|---|
| [onEntry](on-entry.md) | [jvm]<br>open fun [onEntry](on-entry.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Ei](index.md)&gt; |
| [onExit](on-exit.md) | [jvm]<br>open fun [onExit](on-exit.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Ei](index.md)&gt; |
| [transition](transition.md) | [jvm]<br>abstract fun [transition](transition.md)(event: [Ev](index.md)): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[S](index.md), [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Ei](index.md)&gt;&gt; |

## Inheritors

| Name |
|---|
| [PresenceState](../../com.pubnub.api.presence.eventengine.state/-presence-state/index.md) |
| [SubscribeState](../../com.pubnub.api.subscribe.eventengine.state/-subscribe-state/index.md) |
