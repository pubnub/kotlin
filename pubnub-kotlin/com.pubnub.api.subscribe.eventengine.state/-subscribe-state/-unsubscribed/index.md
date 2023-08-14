//[pubnub-kotlin](../../../../index.md)/[com.pubnub.api.subscribe.eventengine.state](../../index.md)/[SubscribeState](../index.md)/[Unsubscribed](index.md)

# Unsubscribed

[jvm]\
object [Unsubscribed](index.md) : [SubscribeState](../index.md)

## Functions

| Name | Summary |
|---|---|
| [onEntry](../../../com.pubnub.api.eventengine/-state/on-entry.md) | [jvm]<br>open fun [onEntry](../../../com.pubnub.api.eventengine/-state/on-entry.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation](../../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md)&gt; |
| [onExit](../../../com.pubnub.api.eventengine/-state/on-exit.md) | [jvm]<br>open fun [onExit](../../../com.pubnub.api.eventengine/-state/on-exit.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation](../../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md)&gt; |
| [transition](transition.md) | [jvm]<br>open override fun [transition](transition.md)(event: [SubscribeEvent](../../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[SubscribeState](../index.md), [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation](../../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md)&gt;&gt; |
