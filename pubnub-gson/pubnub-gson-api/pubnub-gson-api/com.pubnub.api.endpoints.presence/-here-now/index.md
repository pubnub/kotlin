//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.endpoints.presence](../index.md)/[HereNow](index.md)

# HereNow

[jvm]\
interface [HereNow](index.md) : [Endpoint](../../com.pubnub.api.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.endpoints/-endpoint/index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [async](../-where-now/index.md#1418965989%2FFunctions%2F126356644) | [jvm]<br>abstract fun [async](../-where-now/index.md#1418965989%2FFunctions%2F126356644)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [channelGroups](channel-groups.md) | [jvm]<br>abstract fun [channelGroups](channel-groups.md)(channelGroups: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)&gt;): [HereNow](index.md) |
| [channels](channels.md) | [jvm]<br>abstract fun [channels](channels.md)(channels: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)&gt;): [HereNow](index.md) |
| [includeState](include-state.md) | [jvm]<br>abstract fun [includeState](include-state.md)(includeState: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [HereNow](index.md) |
| [includeUUIDs](include-u-u-i-ds.md) | [jvm]<br>abstract fun [includeUUIDs](include-u-u-i-ds.md)(includeUUIDs: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [HereNow](index.md) |
| [operationType](../-where-now/index.md#1414065386%2FFunctions%2F126356644) | [jvm]<br>abstract fun [operationType](../-where-now/index.md#1414065386%2FFunctions%2F126356644)(): [PNOperationType](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](../../com.pubnub.api.endpoints/-endpoint/override-configuration.md) | [jvm]<br>abstract fun [overrideConfiguration](../../com.pubnub.api.endpoints/-endpoint/override-configuration.md)(configuration: [PNConfiguration](../../com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../../com.pubnub.api.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.endpoints/-endpoint/index.md)&gt;<br>Allows to override certain configuration options (see [com.pubnub.api.v2.BasePNConfigurationOverride.Builder](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2/-base-p-n-configuration-override/-builder/index.md)) for this request only. |
| [retry](../-where-now/index.md#2020801116%2FFunctions%2F126356644) | [jvm]<br>abstract fun [retry](../-where-now/index.md#2020801116%2FFunctions%2F126356644)() |
| [silentCancel](../-where-now/index.md#-675955969%2FFunctions%2F126356644) | [jvm]<br>abstract fun [silentCancel](../-where-now/index.md#-675955969%2FFunctions%2F126356644)() |
| [sync](../-where-now/index.md#40193115%2FFunctions%2F126356644) | [jvm]<br>abstract fun [sync](../-where-now/index.md#40193115%2FFunctions%2F126356644)(): [Output](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
