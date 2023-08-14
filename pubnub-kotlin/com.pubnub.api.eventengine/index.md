//[pubnub-kotlin](../../index.md)/[com.pubnub.api.eventengine](index.md)

# Package com.pubnub.api.eventengine

## Types

| Name | Summary |
|---|---|
| [Cancel](-cancel/index.md) | [jvm]<br>data class [Cancel](-cancel/index.md)(val idToCancel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [EffectInvocationType](-effect-invocation-type/index.md) |
| [Effect](-effect/index.md) | [jvm]<br>interface [Effect](-effect/index.md) |
| [EffectDispatcher](-effect-dispatcher/index.md) | [jvm]<br>class [EffectDispatcher](-effect-dispatcher/index.md)&lt;[T](-effect-dispatcher/index.md) : [EffectInvocation](-effect-invocation/index.md)&gt;(effectFactory: [EffectFactory](-effect-factory/index.md)&lt;[T](-effect-dispatcher/index.md)&gt;, effectSource: [Source](-source/index.md)&lt;[T](-effect-dispatcher/index.md)&gt;, managedEffects: [ConcurrentHashMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [ManagedEffect](-managed-effect/index.md)&gt; = ConcurrentHashMap(), executorService: [ExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html) = Executors.newSingleThreadExecutor()) |
| [EffectFactory](-effect-factory/index.md) | [jvm]<br>interface [EffectFactory](-effect-factory/index.md)&lt;[T](-effect-factory/index.md) : [EffectInvocation](-effect-invocation/index.md)&gt; |
| [EffectInvocation](-effect-invocation/index.md) | [jvm]<br>interface [EffectInvocation](-effect-invocation/index.md) |
| [EffectInvocationType](-effect-invocation-type/index.md) | [jvm]<br>interface [EffectInvocationType](-effect-invocation-type/index.md) |
| [Event](-event/index.md) | [jvm]<br>interface [Event](-event/index.md) |
| [EventEngineConf](-event-engine-conf/index.md) | [jvm]<br>interface [EventEngineConf](-event-engine-conf/index.md) |
| [Managed](-managed/index.md) | [jvm]<br>object [Managed](-managed/index.md) : [EffectInvocationType](-effect-invocation-type/index.md) |
| [ManagedEffect](-managed-effect/index.md) | [jvm]<br>interface [ManagedEffect](-managed-effect/index.md) : [Effect](-effect/index.md) |
| [NonManaged](-non-managed/index.md) | [jvm]<br>object [NonManaged](-non-managed/index.md) : [EffectInvocationType](-effect-invocation-type/index.md) |
| [QueueSinkSource](-queue-sink-source/index.md) | [jvm]<br>class [QueueSinkSource](-queue-sink-source/index.md)&lt;[T](-queue-sink-source/index.md)&gt;(queue: [BlockingQueue](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/BlockingQueue.html)&lt;[T](-queue-sink-source/index.md)&gt; = LinkedBlockingQueue()) : [Sink](-sink/index.md)&lt;[T](-queue-sink-source/index.md)&gt; , [Source](-source/index.md)&lt;[T](-queue-sink-source/index.md)&gt; |
| [Sink](-sink/index.md) | [jvm]<br>interface [Sink](-sink/index.md)&lt;[T](-sink/index.md)&gt; |
| [Source](-source/index.md) | [jvm]<br>interface [Source](-source/index.md)&lt;[T](-source/index.md)&gt; |
| [State](-state/index.md) | [jvm]<br>interface [State](-state/index.md)&lt;[T](-state/index.md) : [EffectInvocation](-effect-invocation/index.md), [U](-state/index.md) : [Event](-event/index.md), [V](-state/index.md) : [State](-state/index.md)&lt;[T](-state/index.md), [U](-state/index.md), [V](-state/index.md)&gt;&gt; |

## Functions

| Name | Summary |
|---|---|
| [noTransition](no-transition.md) | [jvm]<br>fun &lt;[T](no-transition.md) : [EffectInvocation](-effect-invocation/index.md), [U](no-transition.md) : [Event](-event/index.md), [V](no-transition.md) : [State](-state/index.md)&lt;[T](no-transition.md), [U](no-transition.md), [V](no-transition.md)&gt;&gt; [V](no-transition.md).[noTransition](no-transition.md)(): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[V](no-transition.md), [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[T](no-transition.md)&gt;&gt; |
| [transitionTo](transition-to.md) | [jvm]<br>fun &lt;[T](transition-to.md) : [EffectInvocation](-effect-invocation/index.md), [U](transition-to.md) : [Event](-event/index.md), [V](transition-to.md) : [State](-state/index.md)&lt;[T](transition-to.md), [U](transition-to.md), [V](transition-to.md)&gt;&gt; [V](transition-to.md).[transitionTo](transition-to.md)(state: [V](transition-to.md), vararg invocations: [T](transition-to.md)): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[V](transition-to.md), [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[T](transition-to.md)&gt;&gt; |
