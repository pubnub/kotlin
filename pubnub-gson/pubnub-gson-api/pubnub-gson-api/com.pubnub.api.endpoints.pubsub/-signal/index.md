//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.endpoints.pubsub](../index.md)/[Signal](index.md)

# Signal

[jvm]\
interface [Signal](index.md) : [Endpoint](../../com.pubnub.api.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.endpoints/-endpoint/index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [async](../../com.pubnub.api.endpoints.presence/-heartbeat/index.md#1418965989%2FFunctions%2F126356644) | [jvm]<br>abstract fun [async](../../com.pubnub.api.endpoints.presence/-heartbeat/index.md#1418965989%2FFunctions%2F126356644)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [channel](channel.md) | [jvm]<br>abstract fun [channel](channel.md)(channel: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [Signal](index.md) |
| [message](message.md) | [jvm]<br>abstract fun [message](message.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Signal](index.md) |
| [operationType](../../com.pubnub.api.endpoints.presence/-heartbeat/index.md#1414065386%2FFunctions%2F126356644) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.presence/-heartbeat/index.md#1414065386%2FFunctions%2F126356644)(): [PNOperationType](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](../../com.pubnub.api.endpoints/-endpoint/override-configuration.md) | [jvm]<br>abstract fun [overrideConfiguration](../../com.pubnub.api.endpoints/-endpoint/override-configuration.md)(configuration: [PNConfiguration](../../com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../../com.pubnub.api.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.endpoints/-endpoint/index.md)&gt;<br>Allows to override certain configuration options (see [com.pubnub.api.v2.BasePNConfigurationOverride.Builder](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2/-base-p-n-configuration-override/-builder/index.md)) for this request only. |
| [retry](../../com.pubnub.api.endpoints.presence/-heartbeat/index.md#2020801116%2FFunctions%2F126356644) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.presence/-heartbeat/index.md#2020801116%2FFunctions%2F126356644)() |
| [silentCancel](../../com.pubnub.api.endpoints.presence/-heartbeat/index.md#-675955969%2FFunctions%2F126356644) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.presence/-heartbeat/index.md#-675955969%2FFunctions%2F126356644)() |
| [sync](../../com.pubnub.api.endpoints.presence/-heartbeat/index.md#40193115%2FFunctions%2F126356644) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.presence/-heartbeat/index.md#40193115%2FFunctions%2F126356644)(): [Output](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
