//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.endpoints.pubsub](../index.md)/[SignalBuilder](index.md)

# SignalBuilder

[jvm]\
interface [SignalBuilder](index.md) : [Signal](../../com.pubnub.api.java.endpoints.pubsub/-signal/index.md)

## Functions

| Name | Summary |
|---|---|
| [async](../-publish-builder/index.md#1418965989%2FFunctions%2F126356644) | [jvm]<br>abstract fun [async](../-publish-builder/index.md#1418965989%2FFunctions%2F126356644)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;Output&gt;&gt;) |
| [channel](../../com.pubnub.api.java.endpoints.pubsub/-signal/channel.md) | [jvm]<br>abstract fun [channel](../../com.pubnub.api.java.endpoints.pubsub/-signal/channel.md)(channel: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [Signal](../../com.pubnub.api.java.endpoints.pubsub/-signal/index.md) |
| [message](../../com.pubnub.api.java.endpoints.pubsub/-signal/message.md) | [jvm]<br>abstract fun [message](../../com.pubnub.api.java.endpoints.pubsub/-signal/message.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Signal](../../com.pubnub.api.java.endpoints.pubsub/-signal/index.md) |
| [operationType](../-publish-builder/index.md#1414065386%2FFunctions%2F126356644) | [jvm]<br>abstract fun [operationType](../-publish-builder/index.md#1414065386%2FFunctions%2F126356644)(): [PNOperationType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](../-publish-builder/index.md#424483198%2FFunctions%2F126356644) | [jvm]<br>abstract fun [overrideConfiguration](../-publish-builder/index.md#424483198%2FFunctions%2F126356644)(configuration: [PNConfiguration](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&gt;<br>Allows to override certain configuration options (see [com.pubnub.api.v2.PNConfigurationOverride.Builder](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only. |
| [retry](../-publish-builder/index.md#2020801116%2FFunctions%2F126356644) | [jvm]<br>abstract fun [retry](../-publish-builder/index.md#2020801116%2FFunctions%2F126356644)() |
| [silentCancel](../-publish-builder/index.md#-675955969%2FFunctions%2F126356644) | [jvm]<br>abstract fun [silentCancel](../-publish-builder/index.md#-675955969%2FFunctions%2F126356644)() |
| [sync](../-publish-builder/index.md#40193115%2FFunctions%2F126356644) | [jvm]<br>abstract fun [sync](../-publish-builder/index.md#40193115%2FFunctions%2F126356644)(): Output |
