//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.eventengine](../index.md)/[EventEngine](index.md)

# EventEngine

[jvm]\
class [EventEngine](index.md)&lt;[Ei](index.md) : [EffectInvocation](../-effect-invocation/index.md), [Ev](index.md) : [Event](../-event/index.md), [S](index.md) : [State](../-state/index.md)&lt;[Ei](index.md), [Ev](index.md), [S](index.md)&gt;&gt;(effectSink: [Sink](../-sink/index.md)&lt;[Ei](index.md)&gt;, eventSource: [Source](../-source/index.md)&lt;[Ev](index.md)&gt;, currentState: [S](index.md), executorService: [ExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html) = Executors.newSingleThreadExecutor())

## Constructors

| | |
|---|---|
| [EventEngine](-event-engine.md) | [jvm]<br>fun &lt;[Ei](index.md) : [EffectInvocation](../-effect-invocation/index.md), [Ev](index.md) : [Event](../-event/index.md), [S](index.md) : [State](../-state/index.md)&lt;[Ei](index.md), [Ev](index.md), [S](index.md)&gt;&gt; [EventEngine](-event-engine.md)(effectSink: [Sink](../-sink/index.md)&lt;[Ei](index.md)&gt;, eventSource: [Source](../-source/index.md)&lt;[Ev](index.md)&gt;, currentState: [S](index.md), executorService: [ExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html) = Executors.newSingleThreadExecutor()) |

## Functions

| Name | Summary |
|---|---|
| [start](start.md) | [jvm]<br>fun [start](start.md)() |
| [stop](stop.md) | [jvm]<br>fun [stop](stop.md)() |
