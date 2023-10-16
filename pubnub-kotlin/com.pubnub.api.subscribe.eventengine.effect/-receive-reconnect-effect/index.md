//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.subscribe.eventengine.effect](../index.md)/[ReceiveReconnectEffect](index.md)

# ReceiveReconnectEffect

[jvm]\
class [ReceiveReconnectEffect](index.md)(receiveMessagesRemoteAction: [RemoteAction](../../com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&lt;[ReceiveMessagesResult](../-receive-messages-result/index.md)&gt;, subscribeEventSink: [Sink](../../com.pubnub.api.eventengine/-sink/index.md)&lt;[SubscribeEvent](../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)&gt;, policy: [RetryPolicy](../-retry-policy/index.md), executorService: [ScheduledExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html), attempts: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)?) : [ManagedEffect](../../com.pubnub.api.eventengine/-managed-effect/index.md)

## Constructors

| | |
|---|---|
| [ReceiveReconnectEffect](-receive-reconnect-effect.md) | [jvm]<br>fun [ReceiveReconnectEffect](-receive-reconnect-effect.md)(receiveMessagesRemoteAction: [RemoteAction](../../com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&lt;[ReceiveMessagesResult](../-receive-messages-result/index.md)&gt;, subscribeEventSink: [Sink](../../com.pubnub.api.eventengine/-sink/index.md)&lt;[SubscribeEvent](../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)&gt;, policy: [RetryPolicy](../-retry-policy/index.md), executorService: [ScheduledExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html), attempts: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)?) |

## Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>open override fun [cancel](cancel.md)() |
| [runEffect](run-effect.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>open override fun [runEffect](run-effect.md)() |
