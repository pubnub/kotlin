//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.endpoints.pubsub](../index.md)/[Publish](index.md)

# Publish

expect interface [Publish](index.md) : [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[PNPublishResult](../../com.pubnub.api.models.consumer/-p-n-publish-result/index.md)&gt; actual interface [Publish](index.md)actual interface [Publish](index.md) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNPublishResult](../../com.pubnub.api.models.consumer/-p-n-publish-result/index.md)&gt; 

#### See also

| |
|---|
| PubNub.publish |
| PubNub.publish |
| PubNub.publish |

#### Inheritors

| |
|---|
| [PublishImpl](../-publish-impl/index.md) |

## Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | [jvm]<br>abstract val [channel](channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [message](message.md) | [jvm]<br>abstract val [message](message.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [meta](meta.md) | [jvm]<br>abstract val [meta](meta.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? |
| [replicate](replicate.md) | [jvm]<br>abstract val [replicate](replicate.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [shouldStore](should-store.md) | [jvm]<br>abstract val [shouldStore](should-store.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? |
| [ttl](ttl.md) | [jvm]<br>abstract val [ttl](ttl.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? |
| [usePost](use-post.md) | [jvm]<br>abstract val [usePost](use-post.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

## Functions

| Name | Summary |
|---|---|
| [alsoAsync](../../com.pubnub.kmp/also-async.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/also-async.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/also-async.md)&gt;.[alsoAsync](../../com.pubnub.kmp/also-async.md)(action: ([T](../../com.pubnub.kmp/also-async.md)) -&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;*&gt;): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/also-async.md)&gt;<br>Execute a second PNFuture after this PNFuture completes successfully, and return the *original* value of this PNFuture after the second PNFuture completes successfully. |
| [async](../../../../../pubnub-kotlin/pubnub-kotlin-api/com.pubnub.api.endpoints.remoteaction/-remote-action/async.md) | [jvm]<br>abstract override fun [async](../-signal/index.md#-508879645%2FFunctions%2F-167468485)(callback: [Consumer](../../com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[PNPublishResult](../../com.pubnub.api.models.consumer/-p-n-publish-result/index.md)&gt;&gt;)<br>Run the action asynchronously, without blocking the calling thread and delivering the result through the [callback](../-signal/index.md#-508879645%2FFunctions%2F-167468485).<br>[common]<br>abstract fun [async](../-signal/index.md#1105225327%2FFunctions%2F-1863117221)(callback: [Consumer](../../com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[PNPublishResult](../../com.pubnub.api.models.consumer/-p-n-publish-result/index.md)&gt;&gt;) |
| [catch](../../com.pubnub.kmp/catch.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/catch.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt;.[catch](../../com.pubnub.kmp/catch.md)(action: ([Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)) -&gt; [Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt;): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt; |
| [map](../../com.pubnub.api.endpoints.remoteaction/map.md) | [jvm]<br>fun &lt;[T](../../com.pubnub.api.endpoints.remoteaction/map.md), [U](../../com.pubnub.api.endpoints.remoteaction/map.md)&gt; [ExtendedRemoteAction](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[T](../../com.pubnub.api.endpoints.remoteaction/map.md)&gt;.[map](../../com.pubnub.api.endpoints.remoteaction/map.md)(function: ([T](../../com.pubnub.api.endpoints.remoteaction/map.md)) -&gt; [U](../../com.pubnub.api.endpoints.remoteaction/map.md)): [ExtendedRemoteAction](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[U](../../com.pubnub.api.endpoints.remoteaction/map.md)&gt; |
| [operationType](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/operation-type.md) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/operation-type.md)(): [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md)<br>Return the type of this operation from the values defined in [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md). |
| [overrideConfiguration](../../com.pubnub.api/-endpoint/override-configuration.md) | [jvm]<br>abstract fun [overrideConfiguration](../../com.pubnub.api/-endpoint/override-configuration.md)(configuration: [PNConfiguration](../../com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNPublishResult](../../com.pubnub.api.models.consumer/-p-n-publish-result/index.md)&gt;<br>abstract fun [overrideConfiguration](../../com.pubnub.api/-endpoint/override-configuration.md)(action: [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNPublishResult](../../com.pubnub.api.models.consumer/-p-n-publish-result/index.md)&gt;<br>Allows to override certain configuration options (see [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only. |
| [remember](../../com.pubnub.kmp/remember.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/remember.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/remember.md)&gt;.[remember](../../com.pubnub.kmp/remember.md)(): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/remember.md)&gt; |
| [retry](../../com.pubnub.api.endpoints.remoteaction/-remote-action/retry.md) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.remoteaction/-remote-action/retry.md)()<br>Attempt to retry the action and deliver the result to a callback registered with a previous call to [async](../../com.pubnub.api/-endpoint/index.md#149557464%2FFunctions%2F-167468485). |
| [silentCancel](../../com.pubnub.api.endpoints.remoteaction/-cancelable/silent-cancel.md) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.remoteaction/-cancelable/silent-cancel.md)()<br>Cancel the action without reporting any further results. |
| [sync](../../com.pubnub.api.endpoints.remoteaction/-remote-action/sync.md) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.remoteaction/-remote-action/sync.md)(): [PNPublishResult](../../com.pubnub.api.models.consumer/-p-n-publish-result/index.md)<br>Run the action synchronously, potentially blocking the calling thread. |
| [then](../../com.pubnub.kmp/then.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/then.md), [U](../../com.pubnub.kmp/then.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/then.md)&gt;.[then](../../com.pubnub.kmp/then.md)(action: ([T](../../com.pubnub.kmp/then.md)) -&gt; [U](../../com.pubnub.kmp/then.md)): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then.md)&gt; |
| [thenAsync](../../com.pubnub.kmp/then-async.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/then-async.md), [U](../../com.pubnub.kmp/then-async.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/then-async.md)&gt;.[thenAsync](../../com.pubnub.kmp/then-async.md)(action: ([T](../../com.pubnub.kmp/then-async.md)) -&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then-async.md)&gt;): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then-async.md)&gt; |
