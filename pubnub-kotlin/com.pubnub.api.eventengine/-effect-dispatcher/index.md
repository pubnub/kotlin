//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.eventengine](../index.md)/[EffectDispatcher](index.md)

# EffectDispatcher

[jvm]\
class [EffectDispatcher](index.md)&lt;[T](index.md) : [EffectInvocation](../-effect-invocation/index.md)&gt;(effectFactory: [EffectFactory](../-effect-factory/index.md)&lt;[T](index.md)&gt;, effectSource: [Source](../-source/index.md)&lt;[T](index.md)&gt;, managedEffects: [ConcurrentHashMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [ManagedEffect](../-managed-effect/index.md)&gt; = ConcurrentHashMap(), executorService: [ExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html) = Executors.newSingleThreadExecutor())

## Constructors

| | |
|---|---|
| [EffectDispatcher](-effect-dispatcher.md) | [jvm]<br>fun &lt;[T](index.md) : [EffectInvocation](../-effect-invocation/index.md)&gt; [EffectDispatcher](-effect-dispatcher.md)(effectFactory: [EffectFactory](../-effect-factory/index.md)&lt;[T](index.md)&gt;, effectSource: [Source](../-source/index.md)&lt;[T](index.md)&gt;, managedEffects: [ConcurrentHashMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [ManagedEffect](../-managed-effect/index.md)&gt; = ConcurrentHashMap(), executorService: [ExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html) = Executors.newSingleThreadExecutor()) |

## Functions

| Name | Summary |
|---|---|
| [start](start.md) | [jvm]<br>fun [start](start.md)() |
| [stop](stop.md) | [jvm]<br>fun [stop](stop.md)() |
