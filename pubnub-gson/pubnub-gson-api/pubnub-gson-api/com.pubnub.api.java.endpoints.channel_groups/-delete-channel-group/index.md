//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.endpoints.channel_groups](../index.md)/[DeleteChannelGroup](index.md)

# DeleteChannelGroup

[jvm]\
interface [DeleteChannelGroup](index.md) : [Endpoint](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [async](index.md#1418965989%2FFunctions%2F126356644) | [jvm]<br>abstract fun [async](index.md#1418965989%2FFunctions%2F126356644)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../../pubnub-kotlin/pubnub-kotlin-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [channelGroup](channel-group.md) | [jvm]<br>abstract fun [channelGroup](channel-group.md)(channelGroup: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [DeleteChannelGroup](index.md) |
| [operationType](index.md#1414065386%2FFunctions%2F126356644) | [jvm]<br>abstract fun [operationType](index.md#1414065386%2FFunctions%2F126356644)(): [PNOperationType](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](index.md#424483198%2FFunctions%2F126356644) | [jvm]<br>abstract fun [overrideConfiguration](index.md#424483198%2FFunctions%2F126356644)(configuration: [PNConfiguration](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&gt;<br>Allows to override certain configuration options (see [com.pubnub.api.v2.PNConfigurationOverride.Builder](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only. |
| [retry](index.md#2020801116%2FFunctions%2F126356644) | [jvm]<br>abstract fun [retry](index.md#2020801116%2FFunctions%2F126356644)() |
| [silentCancel](index.md#-675955969%2FFunctions%2F126356644) | [jvm]<br>abstract fun [silentCancel](index.md#-675955969%2FFunctions%2F126356644)() |
| [sync](index.md#40193115%2FFunctions%2F126356644) | [jvm]<br>abstract fun [sync](index.md#40193115%2FFunctions%2F126356644)(): [Output](../../../../../pubnub-kotlin/pubnub-kotlin-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
