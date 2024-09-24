//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.endpoints.pubsub](../index.md)/[PublishBuilder](index.md)

# PublishBuilder

[jvm]\
interface [PublishBuilder](index.md) : [Endpoint](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [async](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#1418965989%2FFunctions%2F126356644) | [jvm]<br>abstract fun [async](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#1418965989%2FFunctions%2F126356644)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../../pubnub-kotlin/pubnub-kotlin-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [meta](meta.md) | [jvm]<br>abstract fun [meta](meta.md)(meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [PublishBuilder](index.md) |
| [operationType](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#1414065386%2FFunctions%2F126356644) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#1414065386%2FFunctions%2F126356644)(): [PNOperationType](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#424483198%2FFunctions%2F126356644) | [jvm]<br>abstract fun [overrideConfiguration](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#424483198%2FFunctions%2F126356644)(configuration: [PNConfiguration](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&gt;<br>Allows to override certain configuration options (see [com.pubnub.api.v2.PNConfigurationOverride.Builder](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only. |
| [replicate](replicate.md) | [jvm]<br>abstract fun [replicate](replicate.md)(replicate: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PublishBuilder](index.md) |
| [retry](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#2020801116%2FFunctions%2F126356644) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#2020801116%2FFunctions%2F126356644)() |
| [shouldStore](should-store.md) | [jvm]<br>abstract fun [shouldStore](should-store.md)(shouldStore: [Boolean](https://docs.oracle.com/javase/8/docs/api/java/lang/Boolean.html)): [PublishBuilder](index.md) |
| [silentCancel](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#-675955969%2FFunctions%2F126356644) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#-675955969%2FFunctions%2F126356644)() |
| [sync](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#40193115%2FFunctions%2F126356644) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#40193115%2FFunctions%2F126356644)(): [Output](../../../../../pubnub-kotlin/pubnub-kotlin-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
| [ttl](ttl.md) | [jvm]<br>abstract fun [ttl](ttl.md)(ttl: [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)): [PublishBuilder](index.md) |
| [usePOST](use-p-o-s-t.md) | [jvm]<br>abstract fun [usePOST](use-p-o-s-t.md)(usePOST: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PublishBuilder](index.md) |
