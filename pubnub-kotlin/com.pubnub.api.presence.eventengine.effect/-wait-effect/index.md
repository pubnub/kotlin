//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.presence.eventengine.effect](../index.md)/[WaitEffect](index.md)

# WaitEffect

[jvm]\
class [WaitEffect](index.md)(heartbeatInterval: [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html), presenceEventSink: [Sink](../../com.pubnub.api.eventengine/-sink/index.md)&lt;[PresenceEvent](../../com.pubnub.api.presence.eventengine.event/-presence-event/index.md)&gt;, executorService: [ScheduledExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html) = Executors.newSingleThreadScheduledExecutor()) : [ManagedEffect](../../com.pubnub.api.eventengine/-managed-effect/index.md)

## Constructors

| | |
|---|---|
| [WaitEffect](-wait-effect.md) | [jvm]<br>fun [WaitEffect](-wait-effect.md)(heartbeatInterval: [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html), presenceEventSink: [Sink](../../com.pubnub.api.eventengine/-sink/index.md)&lt;[PresenceEvent](../../com.pubnub.api.presence.eventengine.event/-presence-event/index.md)&gt;, executorService: [ScheduledExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html) = Executors.newSingleThreadScheduledExecutor()) |

## Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>open override fun [cancel](cancel.md)() |
| [runEffect](run-effect.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>open override fun [runEffect](run-effect.md)() |
