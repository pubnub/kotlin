//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.eventengine](../index.md)/[SinkSource](index.md)

# SinkSource

[jvm]\
interface [SinkSource](index.md)&lt;[T](index.md)&gt; : [Sink](../-sink/index.md)&lt;[T](index.md)&gt; , [Source](../-source/index.md)&lt;[T](index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [add](../-sink/add.md) | [jvm]<br>abstract fun [add](../-sink/add.md)(item: [T](index.md)) |
| [take](../-source/take.md) | [jvm]<br>abstract fun [take](../-source/take.md)(): [T](index.md) |

## Inheritors

| Name |
|---|
| [QueueSinkSource](../-queue-sink-source/index.md) |
