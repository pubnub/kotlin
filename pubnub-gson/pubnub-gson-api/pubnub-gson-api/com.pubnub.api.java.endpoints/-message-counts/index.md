//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.endpoints](../index.md)/[MessageCounts](index.md)

# MessageCounts

[jvm]\
interface [MessageCounts](index.md) : [Endpoint](../-endpoint/index.md)&lt;[T](../-endpoint/index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [async](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#1418965989%2FFunctions%2F126356644) | [jvm]<br>abstract fun [async](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#1418965989%2FFunctions%2F126356644)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../../pubnub-kotlin/pubnub-kotlin-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [channels](channels.md) | [jvm]<br>abstract fun [channels](channels.md)(channels: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)&gt;): [MessageCounts](index.md) |
| [channelsTimetoken](channels-timetoken.md) | [jvm]<br>abstract fun [channelsTimetoken](channels-timetoken.md)(channelsTimetoken: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[Long](https://docs.oracle.com/javase/8/docs/api/java/lang/Long.html)&gt;): [MessageCounts](index.md) |
| [operationType](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#1414065386%2FFunctions%2F126356644) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#1414065386%2FFunctions%2F126356644)(): [PNOperationType](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#424483198%2FFunctions%2F126356644) | [jvm]<br>abstract fun [overrideConfiguration](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#424483198%2FFunctions%2F126356644)(configuration: [PNConfiguration](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../-endpoint/index.md)&lt;[T](../-endpoint/index.md)&gt;<br>Allows to override certain configuration options (see [com.pubnub.api.v2.PNConfigurationOverride.Builder](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only. |
| [retry](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#2020801116%2FFunctions%2F126356644) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#2020801116%2FFunctions%2F126356644)() |
| [silentCancel](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#-675955969%2FFunctions%2F126356644) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#-675955969%2FFunctions%2F126356644)() |
| [sync](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#40193115%2FFunctions%2F126356644) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md#40193115%2FFunctions%2F126356644)(): [Output](../../../../../pubnub-kotlin/pubnub-kotlin-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
