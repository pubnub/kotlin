//[pubnub-core-api](../../../index.md)/[com.pubnub.api.endpoints.remoteaction](../index.md)/[ComposableRemoteAction](index.md)

# ComposableRemoteAction

[jvm]\
class [ComposableRemoteAction](index.md)&lt;[T](index.md), [U](index.md)&gt;(remoteAction: [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[T](index.md)&gt;, createNextRemoteAction: ([T](index.md)) -&gt; [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[U](index.md)&gt;, checkpoint: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[U](index.md)&gt;

## Constructors

| | |
|---|---|
| [ComposableRemoteAction](-composable-remote-action.md) | [jvm]<br>constructor(remoteAction: [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[T](index.md)&gt;, createNextRemoteAction: ([T](index.md)) -&gt; [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[U](index.md)&gt;, checkpoint: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |
| [ComposableBuilder](-composable-builder/index.md) | [jvm]<br>class [ComposableBuilder](-composable-builder/index.md)&lt;[T](-composable-builder/index.md)&gt;(remoteAction: [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[T](-composable-builder/index.md)&gt;) |

## Functions

| Name | Summary |
|---|---|
| [async](async.md) | [jvm]<br>open override fun [async](async.md)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[U](index.md)&gt;&gt;)<br>Run the action asynchronously, without blocking the calling thread and delivering the result through the [callback](async.md). |
| [checkpoint](checkpoint.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>fun [checkpoint](checkpoint.md)(): [ComposableRemoteAction](index.md)&lt;[T](index.md), [U](index.md)&gt; |
| [map](../map.md) | [jvm]<br>fun &lt;[T](../map.md), [U](../map.md)&gt; [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[T](../map.md)&gt;.[map](../map.md)(function: ([T](../map.md)) -&gt; [U](../map.md)): [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[U](../map.md)&gt; |
| [operationType](operation-type.md) | [jvm]<br>open override fun [operationType](operation-type.md)(): [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md)<br>Return the type of this operation from the values defined in [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md). |
| [retry](retry.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>open override fun [retry](retry.md)()<br>Attempt to retry the action and deliver the result to a callback registered with a previous call to [async](async.md). |
| [silentCancel](silent-cancel.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>open override fun [silentCancel](silent-cancel.md)()<br>Cancel the action without reporting any further results. |
| [sync](sync.md) | [jvm]<br>open override fun [sync](sync.md)(): [U](index.md)<br>Run the action synchronously, potentially blocking the calling thread. |
| [then](then.md) | [jvm]<br>fun &lt;[Y](then.md)&gt; [then](then.md)(factory: ([U](index.md)) -&gt; [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[Y](then.md)&gt;): [ComposableRemoteAction](index.md)&lt;[U](index.md), [Y](then.md)&gt; |
