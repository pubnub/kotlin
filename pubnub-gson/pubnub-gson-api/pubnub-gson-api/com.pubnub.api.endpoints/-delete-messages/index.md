//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.endpoints](../index.md)/[DeleteMessages](index.md)

# DeleteMessages

[jvm]\
interface [DeleteMessages](index.md) : [Endpoint](../-endpoint/index.md)&lt;[T](../-endpoint/index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [async](../../com.pubnub.api.endpoints.presence/-where-now/index.md#1418965989%2FFunctions%2F126356644) | [jvm]<br>abstract fun [async](../../com.pubnub.api.endpoints.presence/-where-now/index.md#1418965989%2FFunctions%2F126356644)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [channels](channels.md) | [jvm]<br>abstract fun [channels](channels.md)(channels: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)&gt;): [DeleteMessages](index.md) |
| [end](end.md) | [jvm]<br>abstract fun [end](end.md)(end: [Long](https://docs.oracle.com/javase/8/docs/api/java/lang/Long.html)): [DeleteMessages](index.md) |
| [operationType](../../com.pubnub.api.endpoints.presence/-where-now/index.md#1414065386%2FFunctions%2F126356644) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.presence/-where-now/index.md#1414065386%2FFunctions%2F126356644)(): [PNOperationType](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](../-endpoint/override-configuration.md) | [jvm]<br>abstract fun [overrideConfiguration](../-endpoint/override-configuration.md)(configuration: [PNConfiguration](../../com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../-endpoint/index.md)&lt;[T](../-endpoint/index.md)&gt;<br>Allows to override certain configuration options (see [com.pubnub.api.v2.BasePNConfigurationOverride.Builder](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2/-base-p-n-configuration-override/-builder/index.md)) for this request only. |
| [retry](../../com.pubnub.api.endpoints.presence/-where-now/index.md#2020801116%2FFunctions%2F126356644) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.presence/-where-now/index.md#2020801116%2FFunctions%2F126356644)() |
| [silentCancel](../../com.pubnub.api.endpoints.presence/-where-now/index.md#-675955969%2FFunctions%2F126356644) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.presence/-where-now/index.md#-675955969%2FFunctions%2F126356644)() |
| [start](start.md) | [jvm]<br>abstract fun [start](start.md)(start: [Long](https://docs.oracle.com/javase/8/docs/api/java/lang/Long.html)): [DeleteMessages](index.md) |
| [sync](../../com.pubnub.api.endpoints.presence/-where-now/index.md#40193115%2FFunctions%2F126356644) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.presence/-where-now/index.md#40193115%2FFunctions%2F126356644)(): [Output](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
