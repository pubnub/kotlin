//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.eventengine](../index.md)/[QueueSinkSource](index.md)

# QueueSinkSource

[jvm]\
class [QueueSinkSource](index.md)&lt;[T](index.md)&gt;(queue: [BlockingQueue](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/BlockingQueue.html)&lt;[T](index.md)&gt; = LinkedBlockingQueue()) : [SinkSource](../-sink-source/index.md)&lt;[T](index.md)&gt;

## Constructors

| | |
|---|---|
| [QueueSinkSource](-queue-sink-source.md) | [jvm]<br>fun &lt;[T](index.md)&gt; [QueueSinkSource](-queue-sink-source.md)(queue: [BlockingQueue](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/BlockingQueue.html)&lt;[T](index.md)&gt; = LinkedBlockingQueue()) |

## Functions

| Name | Summary |
|---|---|
| [add](add.md) | [jvm]<br>open override fun [add](add.md)(item: [T](index.md)) |
| [take](take.md) | [jvm]<br>open override fun [take](take.md)(): [T](index.md) |
