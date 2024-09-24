//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.endpoints.remoteaction](../index.md)/[RemoteAction](index.md)

# RemoteAction

interface [RemoteAction](index.md)&lt;[Output](index.md)&gt; : [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[Output](index.md)&gt; , [Cancelable](../-cancelable/index.md)

#### Inheritors

| |
|---|
| [ExtendedRemoteAction](../-extended-remote-action/index.md) |

## Functions

| Name | Summary |
|---|---|
| [alsoAsync](../../com.pubnub.kmp/also-async.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/also-async.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/also-async.md)&gt;.[alsoAsync](../../com.pubnub.kmp/also-async.md)(action: ([T](../../com.pubnub.kmp/also-async.md)) -&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;*&gt;): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/also-async.md)&gt;<br>Execute a second PNFuture after this PNFuture completes successfully, and return the *original* value of this PNFuture after the second PNFuture completes successfully. |
| [async](async.md) | [common]<br>abstract override fun [async](async.md)(callback: [Consumer](../../com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](index.md)&gt;&gt;)<br>Run the action asynchronously, without blocking the calling thread and delivering the result through the [callback](async.md). |
| [catch](../../com.pubnub.kmp/catch.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/catch.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt;.[catch](../../com.pubnub.kmp/catch.md)(action: ([Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)) -&gt; [Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt;): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt; |
| [remember](../../com.pubnub.kmp/remember.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/remember.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/remember.md)&gt;.[remember](../../com.pubnub.kmp/remember.md)(): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/remember.md)&gt; |
| [retry](retry.md) | [common]<br>abstract fun [retry](retry.md)()<br>Attempt to retry the action and deliver the result to a callback registered with a previous call to [async](async.md). |
| [silentCancel](../-cancelable/silent-cancel.md) | [common]<br>abstract fun [silentCancel](../-cancelable/silent-cancel.md)()<br>Cancel the action without reporting any further results. |
| [sync](sync.md) | [common]<br>abstract fun [sync](sync.md)(): [Output](index.md)<br>Run the action synchronously, potentially blocking the calling thread. |
| [then](../../com.pubnub.kmp/then.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/then.md), [U](../../com.pubnub.kmp/then.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/then.md)&gt;.[then](../../com.pubnub.kmp/then.md)(action: ([T](../../com.pubnub.kmp/then.md)) -&gt; [U](../../com.pubnub.kmp/then.md)): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then.md)&gt; |
| [thenAsync](../../com.pubnub.kmp/then-async.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/then-async.md), [U](../../com.pubnub.kmp/then-async.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/then-async.md)&gt;.[thenAsync](../../com.pubnub.kmp/then-async.md)(action: ([T](../../com.pubnub.kmp/then-async.md)) -&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then-async.md)&gt;): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then-async.md)&gt; |
