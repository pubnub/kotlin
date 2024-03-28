//[pubnub-core-api](../../../index.md)/[com.pubnub.api.endpoints.remoteaction](../index.md)/[MappingRemoteAction](index.md)

# MappingRemoteAction

[jvm]\
class [MappingRemoteAction](index.md)&lt;[T](index.md), [U](index.md)&gt;(remoteAction: [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[T](index.md)&gt;, function: ([T](index.md)) -&gt; [U](index.md)) : [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[U](index.md)&gt;

## Constructors

| | |
|---|---|
| [MappingRemoteAction](-mapping-remote-action.md) | [jvm]<br>constructor(remoteAction: [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[T](index.md)&gt;, function: ([T](index.md)) -&gt; [U](index.md)) |

## Functions

| Name | Summary |
|---|---|
| [async](async.md) | [jvm]<br>open override fun [async](async.md)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[U](index.md)&gt;&gt;)<br>Run the action asynchronously, without blocking the calling thread and delivering the result through the [callback](async.md). |
| [map](../map.md) | [jvm]<br>fun &lt;[T](../map.md), [U](../map.md)&gt; [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[T](../map.md)&gt;.[map](../map.md)(function: ([T](../map.md)) -&gt; [U](../map.md)): [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[U](../map.md)&gt; |
| [operationType](operation-type.md) | [jvm]<br>open override fun [operationType](operation-type.md)(): [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md)<br>Return the type of this operation from the values defined in [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md). |
| [retry](retry.md) | [jvm]<br>open override fun [retry](retry.md)()<br>Attempt to retry the action and deliver the result to a callback registered with a previous call to [async](async.md). |
| [silentCancel](silent-cancel.md) | [jvm]<br>open override fun [silentCancel](silent-cancel.md)()<br>Cancel the action without reporting any further results. |
| [sync](sync.md) | [jvm]<br>open override fun [sync](sync.md)(): [U](index.md)<br>Run the action synchronously, potentially blocking the calling thread. |
