//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.endpoints.pubsub](../index.md)/[Signal](index.md)

# Signal

expect interface [Signal](index.md) : [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[PNPublishResult](../../com.pubnub.api.models.consumer/-p-n-publish-result/index.md)&gt; actual interface [Signal](index.md) : [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[PNPublishResult](../../com.pubnub.api.models.consumer/-p-n-publish-result/index.md)&gt; actual interface [Signal](index.md) : [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[PNPublishResult](../../com.pubnub.api.models.consumer/-p-n-publish-result/index.md)&gt; actual interface [Signal](index.md) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNPublishResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer/-p-n-publish-result/index.md)&gt; 

#### See also

| |
|---|
| PubNub.signal |
| PubNub.signal |
| PubNub.signal |
| PubNub.signal |

## Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | [jvm]<br>abstract val [channel](channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [customMessageType](custom-message-type.md) | [jvm]<br>abstract val [customMessageType](custom-message-type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [message](message.md) | [jvm]<br>abstract val [message](message.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |

## Functions

| Name | Summary |
|---|---|
| async | [jvm]<br>abstract override fun [async](index.md#-508879645%2FFunctions%2F1141030505)(callback: [Consumer](../../com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[PNPublishResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer/-p-n-publish-result/index.md)&gt;&gt;)<br>Run the action asynchronously, without blocking the calling thread and delivering the result through the [callback](index.md#-508879645%2FFunctions%2F1141030505).<br>[common, apple, js]<br>[common]<br>abstract fun [async](index.md#1105225327%2FFunctions%2F1196661149)(callback: [Consumer](../../com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[PNPublishResult](../../com.pubnub.api.models.consumer/-p-n-publish-result/index.md)&gt;&gt;)<br>[apple]<br>abstract fun [async](index.md#1105225327%2FFunctions%2F1581906243)(callback: [Consumer](../../com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[PNPublishResult](../../com.pubnub.api.models.consumer/-p-n-publish-result/index.md)&gt;&gt;)<br>[js]<br>abstract fun [async](index.md#1105225327%2FFunctions%2F1336103183)(callback: [Consumer](../../com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[PNPublishResult](../../com.pubnub.api.models.consumer/-p-n-publish-result/index.md)&gt;&gt;) |
| [operationType](index.md#1414065386%2FFunctions%2F1141030505) | [jvm]<br>abstract fun [operationType](index.md#1414065386%2FFunctions%2F1141030505)(): [PNOperationType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md)<br>Return the type of this operation from the values defined in [PNOperationType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md). |
| [overrideConfiguration](../../com.pubnub.api/-endpoint/override-configuration.md) | [jvm]<br>abstract fun [overrideConfiguration](../../com.pubnub.api/-endpoint/override-configuration.md)(configuration: [PNConfiguration](../../com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNPublishResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer/-p-n-publish-result/index.md)&gt;<br>abstract fun [overrideConfiguration](../../com.pubnub.api/-endpoint/override-configuration.md)(action: [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNPublishResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer/-p-n-publish-result/index.md)&gt;<br>Allows to override certain configuration options (see [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only. |
| [retry](index.md#2020801116%2FFunctions%2F1141030505) | [jvm]<br>abstract fun [retry](index.md#2020801116%2FFunctions%2F1141030505)()<br>Attempt to retry the action and deliver the result to a callback registered with a previous call to [async](../../com.pubnub.api/-endpoint/index.md#149557464%2FFunctions%2F1141030505). |
| [silentCancel](index.md#-675955969%2FFunctions%2F1141030505) | [jvm]<br>abstract fun [silentCancel](index.md#-675955969%2FFunctions%2F1141030505)()<br>Cancel the action without reporting any further results. |
| [sync](index.md#40193115%2FFunctions%2F1141030505) | [jvm]<br>abstract fun [sync](index.md#40193115%2FFunctions%2F1141030505)(): [PNPublishResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer/-p-n-publish-result/index.md)<br>Run the action synchronously, potentially blocking the calling thread. |
