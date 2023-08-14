//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.subscribe.eventengine.effect](../index.md)/[HandshakeReconnectEffect](index.md)

# HandshakeReconnectEffect

[jvm]\
class [HandshakeReconnectEffect](index.md)(remoteAction: [RemoteAction](../../com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&lt;[SubscriptionCursor](../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)&gt;, subscribeEventSink: [Sink](../../com.pubnub.api.eventengine/-sink/index.md)&lt;[SubscribeEvent](../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)&gt;, policy: [RetryPolicy](../-retry-policy/index.md), executorService: [ScheduledExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html), handshakeReconnectInvocation: [SubscribeEffectInvocation.HandshakeReconnect](../-subscribe-effect-invocation/-handshake-reconnect/index.md)) : [ManagedEffect](../../com.pubnub.api.eventengine/-managed-effect/index.md)

## Constructors

| | |
|---|---|
| [HandshakeReconnectEffect](-handshake-reconnect-effect.md) | [jvm]<br>fun [HandshakeReconnectEffect](-handshake-reconnect-effect.md)(remoteAction: [RemoteAction](../../com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&lt;[SubscriptionCursor](../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)&gt;, subscribeEventSink: [Sink](../../com.pubnub.api.eventengine/-sink/index.md)&lt;[SubscribeEvent](../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)&gt;, policy: [RetryPolicy](../-retry-policy/index.md), executorService: [ScheduledExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html), handshakeReconnectInvocation: [SubscribeEffectInvocation.HandshakeReconnect](../-subscribe-effect-invocation/-handshake-reconnect/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>open override fun [cancel](cancel.md)() |
| [runEffect](run-effect.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>open override fun [runEffect](run-effect.md)() |
