//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.endpoints.presence](../index.md)/[WhereNow](index.md)

# WhereNow

expect interface [WhereNow](index.md) : [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[PNWhereNowResult](../../com.pubnub.api.models.consumer.presence/-p-n-where-now-result/index.md)&gt; actual interface [WhereNow](index.md)actual interface [WhereNow](index.md) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNWhereNowResult](../../com.pubnub.api.models.consumer.presence/-p-n-where-now-result/index.md)&gt; 

#### See also

| |
|---|
| PubNub.whereNow |
| PubNub.whereNow |
| PubNub.whereNow |

#### Inheritors

| |
|---|
| [WhereNowImpl](../-where-now-impl/index.md) |

## Properties

| Name | Summary |
|---|---|
| [uuid](uuid.md) | [jvm]<br>abstract val [uuid](uuid.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Functions

| Name | Summary |
|---|---|
| [alsoAsync](../../com.pubnub.kmp/also-async.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/also-async.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/also-async.md)&gt;.[alsoAsync](../../com.pubnub.kmp/also-async.md)(action: ([T](../../com.pubnub.kmp/also-async.md)) -&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;*&gt;): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/also-async.md)&gt;<br>Execute a second PNFuture after this PNFuture completes successfully, and return the *original* value of this PNFuture after the second PNFuture completes successfully. |
| [async](../../../../../pubnub-kotlin/pubnub-kotlin-api/com.pubnub.api.endpoints.remoteaction/-remote-action/async.md) | [jvm]<br>abstract override fun [async](index.md#498188500%2FFunctions%2F-167468485)(callback: [Consumer](../../com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[PNWhereNowResult](../../com.pubnub.api.models.consumer.presence/-p-n-where-now-result/index.md)&gt;&gt;)<br>Run the action asynchronously, without blocking the calling thread and delivering the result through the [callback](index.md#498188500%2FFunctions%2F-167468485).<br>[common]<br>abstract fun [async](index.md#598838112%2FFunctions%2F-1863117221)(callback: [Consumer](../../com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[PNWhereNowResult](../../com.pubnub.api.models.consumer.presence/-p-n-where-now-result/index.md)&gt;&gt;) |
| [catch](../../com.pubnub.kmp/catch.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/catch.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt;.[catch](../../com.pubnub.kmp/catch.md)(action: ([Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)) -&gt; [Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt;): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt; |
| [map](../../com.pubnub.api.endpoints.remoteaction/map.md) | [jvm]<br>fun &lt;[T](../../com.pubnub.api.endpoints.remoteaction/map.md), [U](../../com.pubnub.api.endpoints.remoteaction/map.md)&gt; [ExtendedRemoteAction](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[T](../../com.pubnub.api.endpoints.remoteaction/map.md)&gt;.[map](../../com.pubnub.api.endpoints.remoteaction/map.md)(function: ([T](../../com.pubnub.api.endpoints.remoteaction/map.md)) -&gt; [U](../../com.pubnub.api.endpoints.remoteaction/map.md)): [ExtendedRemoteAction](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[U](../../com.pubnub.api.endpoints.remoteaction/map.md)&gt; |
| [operationType](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/operation-type.md) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/operation-type.md)(): [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md)<br>Return the type of this operation from the values defined in [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md). |
| [overrideConfiguration](../../com.pubnub.api/-endpoint/override-configuration.md) | [jvm]<br>abstract fun [overrideConfiguration](../../com.pubnub.api/-endpoint/override-configuration.md)(configuration: [PNConfiguration](../../com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNWhereNowResult](../../com.pubnub.api.models.consumer.presence/-p-n-where-now-result/index.md)&gt;<br>abstract fun [overrideConfiguration](../../com.pubnub.api/-endpoint/override-configuration.md)(action: [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNWhereNowResult](../../com.pubnub.api.models.consumer.presence/-p-n-where-now-result/index.md)&gt;<br>Allows to override certain configuration options (see [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only. |
| [remember](../../com.pubnub.kmp/remember.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/remember.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/remember.md)&gt;.[remember](../../com.pubnub.kmp/remember.md)(): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/remember.md)&gt; |
| [retry](../../com.pubnub.api.endpoints.remoteaction/-remote-action/retry.md) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.remoteaction/-remote-action/retry.md)()<br>Attempt to retry the action and deliver the result to a callback registered with a previous call to [async](../../com.pubnub.api/-endpoint/index.md#149557464%2FFunctions%2F-167468485). |
| [silentCancel](../../com.pubnub.api.endpoints.remoteaction/-cancelable/silent-cancel.md) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.remoteaction/-cancelable/silent-cancel.md)()<br>Cancel the action without reporting any further results. |
| [sync](../../com.pubnub.api.endpoints.remoteaction/-remote-action/sync.md) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.remoteaction/-remote-action/sync.md)(): [PNWhereNowResult](../../com.pubnub.api.models.consumer.presence/-p-n-where-now-result/index.md)<br>Run the action synchronously, potentially blocking the calling thread. |
| [then](../../com.pubnub.kmp/then.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/then.md), [U](../../com.pubnub.kmp/then.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/then.md)&gt;.[then](../../com.pubnub.kmp/then.md)(action: ([T](../../com.pubnub.kmp/then.md)) -&gt; [U](../../com.pubnub.kmp/then.md)): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then.md)&gt; |
| [thenAsync](../../com.pubnub.kmp/then-async.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/then-async.md), [U](../../com.pubnub.kmp/then-async.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/then-async.md)&gt;.[thenAsync](../../com.pubnub.kmp/then-async.md)(action: ([T](../../com.pubnub.kmp/then-async.md)) -&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then-async.md)&gt;): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then-async.md)&gt; |
