//[pubnub-kotlin](../../index.md)/[com.pubnub.api.presence.eventengine.effect](index.md)

# Package com.pubnub.api.presence.eventengine.effect

## Types

| Name | Summary |
|---|---|
| [HeartbeatEffect](-heartbeat-effect/index.md) | [jvm]<br>class [HeartbeatEffect](-heartbeat-effect/index.md)(heartbeatRemoteAction: [RemoteAction](../com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt;, presenceEventSink: [Sink](../com.pubnub.api.eventengine/-sink/index.md)&lt;[PresenceEvent](../com.pubnub.api.presence.eventengine.event/-presence-event/index.md)&gt;) : [Effect](../com.pubnub.api.eventengine/-effect/index.md) |
| [LeaveEffect](-leave-effect/index.md) | [jvm]<br>class [LeaveEffect](-leave-effect/index.md)(leaveRemoteAction: [RemoteAction](../com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt;) : [Effect](../com.pubnub.api.eventengine/-effect/index.md) |
| [PresenceEffectInvocation](-presence-effect-invocation/index.md) | [jvm]<br>sealed class [PresenceEffectInvocation](-presence-effect-invocation/index.md) : [EffectInvocation](../com.pubnub.api.eventengine/-effect-invocation/index.md) |
| [WaitEffect](-wait-effect/index.md) | [jvm]<br>class [WaitEffect](-wait-effect/index.md)(heartbeatInterval: [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html), presenceEventSink: [Sink](../com.pubnub.api.eventengine/-sink/index.md)&lt;[PresenceEvent](../com.pubnub.api.presence.eventengine.event/-presence-event/index.md)&gt;, executorService: [ScheduledExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html) = Executors.newSingleThreadScheduledExecutor()) : [ManagedEffect](../com.pubnub.api.eventengine/-managed-effect/index.md) |
