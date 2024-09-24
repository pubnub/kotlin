//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.endpoints.remoteaction](../index.md)/[ExtendedRemoteAction](index.md)

# ExtendedRemoteAction

interface [ExtendedRemoteAction](index.md)&lt;[Output](index.md)&gt; : [RemoteAction](../-remote-action/index.md)&lt;[Output](index.md)&gt; 

#### Inheritors

| |
|---|
| [Endpoint](../../com.pubnub.api/-endpoint/index.md#-1898379228%2FMain%2F-1863117221) |
| [SendFile](../../com.pubnub.api.endpoints.files/-send-file/index.md) |
| [ComposableRemoteAction](../-composable-remote-action/index.md#498592380%2FMain%2F-1863117221) |
| [MappingRemoteAction](../-mapping-remote-action/index.md#849613811%2FMain%2F-1863117221) |

## Functions

| Name | Summary |
|---|---|
| [alsoAsync](../../com.pubnub.kmp/also-async.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/also-async.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/also-async.md)&gt;.[alsoAsync](../../com.pubnub.kmp/also-async.md)(action: ([T](../../com.pubnub.kmp/also-async.md)) -&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;*&gt;): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/also-async.md)&gt;<br>Execute a second PNFuture after this PNFuture completes successfully, and return the *original* value of this PNFuture after the second PNFuture completes successfully. |
| [async](../-remote-action/async.md) | [common]<br>abstract override fun [async](../-remote-action/async.md)(callback: [Consumer](../../com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](index.md)&gt;&gt;)<br>Run the action asynchronously, without blocking the calling thread and delivering the result through the [callback](../-remote-action/async.md). |
| [catch](../../com.pubnub.kmp/catch.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/catch.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt;.[catch](../../com.pubnub.kmp/catch.md)(action: ([Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)) -&gt; [Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt;): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt; |
| [map](../map.md) | [jvm]<br>fun &lt;[T](../map.md), [U](../map.md)&gt; [ExtendedRemoteAction](index.md)&lt;[T](../map.md)&gt;.[map](../map.md)(function: ([T](../map.md)) -&gt; [U](../map.md)): [ExtendedRemoteAction](index.md)&lt;[U](../map.md)&gt; |
| [operationType](operation-type.md) | [common]<br>abstract fun [operationType](operation-type.md)(): [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md)<br>Return the type of this operation from the values defined in [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md). |
| [remember](../../com.pubnub.kmp/remember.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/remember.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/remember.md)&gt;.[remember](../../com.pubnub.kmp/remember.md)(): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/remember.md)&gt; |
| [retry](../-remote-action/retry.md) | [common]<br>abstract fun [retry](../-remote-action/retry.md)()<br>Attempt to retry the action and deliver the result to a callback registered with a previous call to [async](../-remote-action/async.md). |
| [silentCancel](../-cancelable/silent-cancel.md) | [common]<br>abstract fun [silentCancel](../-cancelable/silent-cancel.md)()<br>Cancel the action without reporting any further results. |
| [sync](../-remote-action/sync.md) | [common]<br>abstract fun [sync](../-remote-action/sync.md)(): [Output](index.md)<br>Run the action synchronously, potentially blocking the calling thread. |
| [then](../../com.pubnub.kmp/then.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/then.md), [U](../../com.pubnub.kmp/then.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/then.md)&gt;.[then](../../com.pubnub.kmp/then.md)(action: ([T](../../com.pubnub.kmp/then.md)) -&gt; [U](../../com.pubnub.kmp/then.md)): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then.md)&gt; |
| [thenAsync](../../com.pubnub.kmp/then-async.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/then-async.md), [U](../../com.pubnub.kmp/then-async.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/then-async.md)&gt;.[thenAsync](../../com.pubnub.kmp/then-async.md)(action: ([T](../../com.pubnub.kmp/then-async.md)) -&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then-async.md)&gt;): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then-async.md)&gt; |
