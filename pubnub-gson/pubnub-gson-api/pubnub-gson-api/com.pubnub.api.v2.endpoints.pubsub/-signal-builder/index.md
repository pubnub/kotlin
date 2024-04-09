//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.v2.endpoints.pubsub](../index.md)/[SignalBuilder](index.md)

# SignalBuilder

[jvm]\
interface [SignalBuilder](index.md) : [Endpoint](../../com.pubnub.api.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.endpoints/-endpoint/index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [async](index.md#1418965989%2FFunctions%2F126356644) | [jvm]<br>abstract fun [async](index.md#1418965989%2FFunctions%2F126356644)(p: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [operationType](index.md#1414065386%2FFunctions%2F126356644) | [jvm]<br>abstract fun [operationType](index.md#1414065386%2FFunctions%2F126356644)(): [PNOperationType](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](../../com.pubnub.api.endpoints/-endpoint/override-configuration.md) | [jvm]<br>abstract fun [overrideConfiguration](../../com.pubnub.api.endpoints/-endpoint/override-configuration.md)(configuration: [PNConfiguration](../../com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../../com.pubnub.api.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.endpoints/-endpoint/index.md)&gt;<br>Allows to override certain configuration options (see [com.pubnub.api.v2.BasePNConfigurationOverride.Builder](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2/-base-p-n-configuration-override/-builder/index.md)) for this request only. |
| [retry](index.md#2020801116%2FFunctions%2F126356644) | [jvm]<br>abstract fun [retry](index.md#2020801116%2FFunctions%2F126356644)() |
| [silentCancel](index.md#-675955969%2FFunctions%2F126356644) | [jvm]<br>abstract fun [silentCancel](index.md#-675955969%2FFunctions%2F126356644)() |
| [sync](index.md#40193115%2FFunctions%2F126356644) | [jvm]<br>abstract fun [sync](index.md#40193115%2FFunctions%2F126356644)(): [Output](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
