//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.endpoints](../index.md)/[History](index.md)

# History

[jvm]\
interface [History](index.md) : [Endpoint](../-endpoint/index.md)&lt;[T](../-endpoint/index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [async](../../com.pubnub.api.endpoints.access.builder/-grant-token-entities-builder/index.md#1418965989%2FFunctions%2F126356644) | [jvm]<br>abstract fun [async](../../com.pubnub.api.endpoints.access.builder/-grant-token-entities-builder/index.md#1418965989%2FFunctions%2F126356644)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [channel](channel.md) | [jvm]<br>abstract fun [channel](channel.md)(channel: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [History](index.md) |
| [count](count.md) | [jvm]<br>abstract fun [count](count.md)(count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [History](index.md) |
| [end](end.md) | [jvm]<br>abstract fun [end](end.md)(end: [Long](https://docs.oracle.com/javase/8/docs/api/java/lang/Long.html)): [History](index.md) |
| [includeMeta](include-meta.md) | [jvm]<br>abstract fun [includeMeta](include-meta.md)(includeMeta: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [History](index.md) |
| [includeTimetoken](include-timetoken.md) | [jvm]<br>abstract fun [includeTimetoken](include-timetoken.md)(includeTimetoken: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [History](index.md) |
| [operationType](../../com.pubnub.api.endpoints.access.builder/-grant-token-entities-builder/index.md#1414065386%2FFunctions%2F126356644) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.access.builder/-grant-token-entities-builder/index.md#1414065386%2FFunctions%2F126356644)(): [PNOperationType](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](../-endpoint/override-configuration.md) | [jvm]<br>abstract fun [overrideConfiguration](../-endpoint/override-configuration.md)(configuration: [PNConfiguration](../../com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../-endpoint/index.md)&lt;[T](../-endpoint/index.md)&gt;<br>Allows to override certain configuration options (see [com.pubnub.api.v2.BasePNConfigurationOverride.Builder](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2/-base-p-n-configuration-override/-builder/index.md)) for this request only. |
| [retry](../../com.pubnub.api.endpoints.access.builder/-grant-token-entities-builder/index.md#2020801116%2FFunctions%2F126356644) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.access.builder/-grant-token-entities-builder/index.md#2020801116%2FFunctions%2F126356644)() |
| [reverse](reverse.md) | [jvm]<br>abstract fun [reverse](reverse.md)(reverse: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [History](index.md) |
| [silentCancel](../../com.pubnub.api.endpoints.access.builder/-grant-token-entities-builder/index.md#-675955969%2FFunctions%2F126356644) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.access.builder/-grant-token-entities-builder/index.md#-675955969%2FFunctions%2F126356644)() |
| [start](start.md) | [jvm]<br>abstract fun [start](start.md)(start: [Long](https://docs.oracle.com/javase/8/docs/api/java/lang/Long.html)): [History](index.md) |
| [sync](../../com.pubnub.api.endpoints.access.builder/-grant-token-entities-builder/index.md#40193115%2FFunctions%2F126356644) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.access.builder/-grant-token-entities-builder/index.md#40193115%2FFunctions%2F126356644)(): [Output](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
