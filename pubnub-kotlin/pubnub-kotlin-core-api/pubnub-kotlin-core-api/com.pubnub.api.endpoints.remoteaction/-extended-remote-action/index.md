//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.endpoints.remoteaction](../index.md)/[ExtendedRemoteAction](index.md)

# ExtendedRemoteAction

interface [ExtendedRemoteAction](index.md)&lt;[Output](index.md)&gt; : [RemoteAction](../-remote-action/index.md)&lt;[Output](index.md)&gt; 

#### Inheritors

| |
|---|
| [Endpoint](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-endpoint/index.md) |

## Functions

| Name | Summary |
|---|---|
| [async](../-remote-action/async.md) | [common]<br>abstract override fun [async](../-remote-action/async.md)(callback: [Consumer](../../com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](index.md)&gt;&gt;)<br>Run the action asynchronously, without blocking the calling thread and delivering the result through the [callback](../-remote-action/async.md). |
| [operationType](operation-type.md) | [common]<br>abstract fun [operationType](operation-type.md)(): [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md)<br>Return the type of this operation from the values defined in [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md). |
| [retry](../-remote-action/retry.md) | [common]<br>abstract fun [retry](../-remote-action/retry.md)()<br>Attempt to retry the action and deliver the result to a callback registered with a previous call to [async](../-remote-action/async.md). |
| [silentCancel](../-cancelable/silent-cancel.md) | [common]<br>abstract fun [silentCancel](../-cancelable/silent-cancel.md)()<br>Cancel the action without reporting any further results. |
| [sync](../-remote-action/sync.md) | [common]<br>abstract fun [sync](../-remote-action/sync.md)(): [Output](index.md)<br>Run the action synchronously, potentially blocking the calling thread. |
