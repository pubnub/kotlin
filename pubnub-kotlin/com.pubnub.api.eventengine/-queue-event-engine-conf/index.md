//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.eventengine](../index.md)/[QueueEventEngineConf](index.md)

# QueueEventEngineConf

[jvm]\
class [QueueEventEngineConf](index.md)&lt;[Ei](index.md) : [EffectInvocation](../-effect-invocation/index.md), [Ev](index.md) : [Event](../-event/index.md)&gt;(effectSinkSource: [SinkSource](../-sink-source/index.md)&lt;[Ei](index.md)&gt; = QueueSinkSource(), eventSinkSource: [SinkSource](../-sink-source/index.md)&lt;[Ev](index.md)&gt; = QueueSinkSource()) : [EventEngineConf](../-event-engine-conf/index.md)&lt;[Ei](index.md), [Ev](index.md)&gt;

## Constructors

| | |
|---|---|
| [QueueEventEngineConf](-queue-event-engine-conf.md) | [jvm]<br>fun &lt;[Ei](index.md) : [EffectInvocation](../-effect-invocation/index.md), [Ev](index.md) : [Event](../-event/index.md)&gt; [QueueEventEngineConf](-queue-event-engine-conf.md)(effectSinkSource: [SinkSource](../-sink-source/index.md)&lt;[Ei](index.md)&gt; = QueueSinkSource(), eventSinkSource: [SinkSource](../-sink-source/index.md)&lt;[Ev](index.md)&gt; = QueueSinkSource()) |

## Properties

| Name | Summary |
|---|---|
| [effectSink](effect-sink.md) | [jvm]<br>open override val [effectSink](effect-sink.md): [Sink](../-sink/index.md)&lt;[Ei](index.md)&gt; |
| [effectSource](effect-source.md) | [jvm]<br>open override val [effectSource](effect-source.md): [Source](../-source/index.md)&lt;[Ei](index.md)&gt; |
| [eventSink](event-sink.md) | [jvm]<br>open override val [eventSink](event-sink.md): [Sink](../-sink/index.md)&lt;[Ev](index.md)&gt; |
| [eventSource](event-source.md) | [jvm]<br>open override val [eventSource](event-source.md): [Source](../-source/index.md)&lt;[Ev](index.md)&gt; |
