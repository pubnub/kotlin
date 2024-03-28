//[pubnub-core-api](../../../index.md)/[com.pubnub.api.endpoints.remoteaction](../index.md)/[RemoteAction](index.md)

# RemoteAction

interface [RemoteAction](index.md)&lt;[Output](index.md)&gt; : [Cancelable](../-cancelable/index.md)

#### Inheritors

| |
|---|
| [ExtendedRemoteAction](../-extended-remote-action/index.md) |

## Functions

| Name | Summary |
|---|---|
| [async](async.md) | [jvm]<br>abstract fun [async](async.md)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](index.md)&gt;&gt;)<br>Run the action asynchronously, without blocking the calling thread and delivering the result through the [callback](async.md). |
| [retry](retry.md) | [jvm]<br>abstract fun [retry](retry.md)()<br>Attempt to retry the action and deliver the result to a callback registered with a previous call to [async](async.md). |
| [silentCancel](../-cancelable/silent-cancel.md) | [jvm]<br>abstract fun [silentCancel](../-cancelable/silent-cancel.md)()<br>Cancel the action without reporting any further results. |
| [sync](sync.md) | [jvm]<br>abstract fun [sync](sync.md)(): [Output](index.md)<br>Run the action synchronously, potentially blocking the calling thread. |
