//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.endpoints.pubsub](../index.md)/[PublishBuilder](index.md)

# PublishBuilder

[jvm]\
interface [PublishBuilder](index.md) : [Endpoint](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&gt; 

Interface representing a builder for configuring a publish operation. This interface extends [Endpoint](../../com.pubnub.api.java.endpoints/-endpoint/index.md) to provide a fluent API for setting various parameters for the publish request.

## Functions

| Name | Summary |
|---|---|
| [async](../-signal-builder/index.md#1418965989%2FFunctions%2F126356644) | [jvm]<br>abstract fun [async](../-signal-builder/index.md#1418965989%2FFunctions%2F126356644)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;Output&gt;&gt;) |
| [customMessageType](custom-message-type.md) | [jvm]<br>abstract fun [customMessageType](custom-message-type.md)(customMessageType: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [PublishBuilder](index.md)<br>Specifies a custom message type for the message. |
| [meta](meta.md) | [jvm]<br>abstract fun [meta](meta.md)(meta: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)): [PublishBuilder](index.md)<br>Sets the metadata to be sent along with the message. |
| [operationType](../-signal-builder/index.md#1414065386%2FFunctions%2F126356644) | [jvm]<br>abstract fun [operationType](../-signal-builder/index.md#1414065386%2FFunctions%2F126356644)(): [PNOperationType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](../-signal-builder/index.md#424483198%2FFunctions%2F126356644) | [jvm]<br>abstract fun [overrideConfiguration](../-signal-builder/index.md#424483198%2FFunctions%2F126356644)(configuration: [PNConfiguration](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&gt;<br>Allows to override certain configuration options (see [com.pubnub.api.v2.PNConfigurationOverride.Builder](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only. |
| [replicate](replicate.md) | [jvm]<br>abstract fun [replicate](replicate.md)(replicate: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)): [PublishBuilder](index.md)<br>Specifies whether the message should be replicated across datacenters. |
| [retry](../-signal-builder/index.md#2020801116%2FFunctions%2F126356644) | [jvm]<br>abstract fun [retry](../-signal-builder/index.md#2020801116%2FFunctions%2F126356644)() |
| [shouldStore](should-store.md) | [jvm]<br>abstract fun [shouldStore](should-store.md)(shouldStore: [Boolean](https://docs.oracle.com/javase/8/docs/api/java/lang/Boolean.html)): [PublishBuilder](index.md)<br>Specifies whether the message should be stored in the history of the channel. |
| [silentCancel](../-signal-builder/index.md#-675955969%2FFunctions%2F126356644) | [jvm]<br>abstract fun [silentCancel](../-signal-builder/index.md#-675955969%2FFunctions%2F126356644)() |
| [sync](../-signal-builder/index.md#40193115%2FFunctions%2F126356644) | [jvm]<br>abstract fun [sync](../-signal-builder/index.md#40193115%2FFunctions%2F126356644)(): Output |
| [ttl](ttl.md) | [jvm]<br>abstract fun [ttl](ttl.md)(ttl: [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)): [PublishBuilder](index.md)<br>Sets the time-to-live (TTL) in Message Persistence. |
| [usePOST](use-p-o-s-t.md) | [jvm]<br>abstract fun [usePOST](use-p-o-s-t.md)(usePOST: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)): [PublishBuilder](index.md)<br>Configures the publish request to use the POST HTTP method instead of GET. |
