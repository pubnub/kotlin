//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.eventengine](../index.md)/[EventEngineConf](index.md)

# EventEngineConf

[jvm]\
interface [EventEngineConf](index.md)&lt;[Ei](index.md) : [EffectInvocation](../-effect-invocation/index.md), [Ev](index.md) : [Event](../-event/index.md)&gt;

## Properties

| Name | Summary |
|---|---|
| [effectSink](effect-sink.md) | [jvm]<br>abstract val [effectSink](effect-sink.md): [Sink](../-sink/index.md)&lt;[Ei](index.md)&gt; |
| [effectSource](effect-source.md) | [jvm]<br>abstract val [effectSource](effect-source.md): [Source](../-source/index.md)&lt;[Ei](index.md)&gt; |
| [eventSink](event-sink.md) | [jvm]<br>abstract val [eventSink](event-sink.md): [Sink](../-sink/index.md)&lt;[Ev](index.md)&gt; |
| [eventSource](event-source.md) | [jvm]<br>abstract val [eventSource](event-source.md): [Source](../-source/index.md)&lt;[Ev](index.md)&gt; |

## Inheritors

| Name |
|---|
| [QueueEventEngineConf](../-queue-event-engine-conf/index.md) |
