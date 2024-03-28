//[pubnub-core-api](../../../index.md)/[com.pubnub.api.endpoints.remoteaction](../index.md)/[ExtendedRemoteAction](index.md)

# ExtendedRemoteAction

interface [ExtendedRemoteAction](index.md)&lt;[Output](index.md)&gt; : [RemoteAction](../-remote-action/index.md)&lt;[Output](index.md)&gt; 

#### Inheritors

| |
|---|
| [ComposableRemoteAction](../-composable-remote-action/index.md) |
| [MappingRemoteAction](../-mapping-remote-action/index.md) |

## Functions

| Name | Summary |
|---|---|
| [async](../-remote-action/async.md) | [jvm]<br>abstract fun [async](../-remote-action/async.md)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](index.md)&gt;&gt;)<br>Run the action asynchronously, without blocking the calling thread and delivering the result through the [callback](../-remote-action/async.md). |
| [map](../map.md) | [jvm]<br>fun &lt;[T](../map.md), [U](../map.md)&gt; [ExtendedRemoteAction](index.md)&lt;[T](../map.md)&gt;.[map](../map.md)(function: ([T](../map.md)) -&gt; [U](../map.md)): [ExtendedRemoteAction](index.md)&lt;[U](../map.md)&gt; |
| [operationType](operation-type.md) | [jvm]<br>abstract fun [operationType](operation-type.md)(): [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md)<br>Return the type of this operation from the values defined in [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md). |
| [retry](../-remote-action/retry.md) | [jvm]<br>abstract fun [retry](../-remote-action/retry.md)()<br>Attempt to retry the action and deliver the result to a callback registered with a previous call to [async](../-remote-action/async.md). |
| [silentCancel](../-cancelable/silent-cancel.md) | [jvm]<br>abstract fun [silentCancel](../-cancelable/silent-cancel.md)()<br>Cancel the action without reporting any further results. |
| [sync](../-remote-action/sync.md) | [jvm]<br>abstract fun [sync](../-remote-action/sync.md)(): [Output](index.md)<br>Run the action synchronously, potentially blocking the calling thread. |
