//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.eventengine](../index.md)/[EventEngineManager](index.md)

# EventEngineManager

[jvm]\
class [EventEngineManager](index.md)&lt;[Ei](index.md) : [EffectInvocation](../-effect-invocation/index.md), [Ev](index.md) : [Event](../-event/index.md), [S](index.md) : [State](../-state/index.md)&lt;[Ei](index.md), [Ev](index.md), [S](index.md)&gt;, [Ee](index.md) : [EventEngine](../-event-engine/index.md)&lt;[Ei](index.md), [Ev](index.md), [S](index.md)&gt;&gt;(eventEngine: [Ee](index.md), effectDispatcher: [EffectDispatcher](../-effect-dispatcher/index.md)&lt;[Ei](index.md)&gt;, eventSink: [Sink](../-sink/index.md)&lt;[Ev](index.md)&gt;)

## Constructors

| | |
|---|---|
| [EventEngineManager](-event-engine-manager.md) | [jvm]<br>fun &lt;[Ei](index.md) : [EffectInvocation](../-effect-invocation/index.md), [Ev](index.md) : [Event](../-event/index.md), [Ee](index.md) : [EventEngine](../-event-engine/index.md)&lt;[Ei](index.md), [Ev](index.md), [S](index.md)&gt;&gt; [EventEngineManager](-event-engine-manager.md)(eventEngine: [Ee](index.md), effectDispatcher: [EffectDispatcher](../-effect-dispatcher/index.md)&lt;[Ei](index.md)&gt;, eventSink: [Sink](../-sink/index.md)&lt;[Ev](index.md)&gt;) |

## Functions

| Name | Summary |
|---|---|
| [addEventToQueue](add-event-to-queue.md) | [jvm]<br>fun [addEventToQueue](add-event-to-queue.md)(event: [Ev](index.md)) |
| [start](start.md) | [jvm]<br>fun [start](start.md)() |
| [stop](stop.md) | [jvm]<br>fun [stop](stop.md)() |
