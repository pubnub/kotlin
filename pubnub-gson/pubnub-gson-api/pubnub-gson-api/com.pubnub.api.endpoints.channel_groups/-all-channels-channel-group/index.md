//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.endpoints.channel_groups](../index.md)/[AllChannelsChannelGroup](index.md)

# AllChannelsChannelGroup

[jvm]\
interface [AllChannelsChannelGroup](index.md) : [Endpoint](../../com.pubnub.api.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.endpoints/-endpoint/index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [async](../-list-all-channel-group/index.md#1418965989%2FFunctions%2F126356644) | [jvm]<br>abstract fun [async](../-list-all-channel-group/index.md#1418965989%2FFunctions%2F126356644)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [channelGroup](channel-group.md) | [jvm]<br>abstract fun [channelGroup](channel-group.md)(channelGroup: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [AllChannelsChannelGroup](index.md) |
| [operationType](../-list-all-channel-group/index.md#1414065386%2FFunctions%2F126356644) | [jvm]<br>abstract fun [operationType](../-list-all-channel-group/index.md#1414065386%2FFunctions%2F126356644)(): [PNOperationType](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](../../com.pubnub.api.endpoints/-endpoint/override-configuration.md) | [jvm]<br>abstract fun [overrideConfiguration](../../com.pubnub.api.endpoints/-endpoint/override-configuration.md)(configuration: [PNConfiguration](../../com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../../com.pubnub.api.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.endpoints/-endpoint/index.md)&gt;<br>Allows to override certain configuration options (see [com.pubnub.api.v2.BasePNConfigurationOverride.Builder](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2/-base-p-n-configuration-override/-builder/index.md)) for this request only. |
| [retry](../-list-all-channel-group/index.md#2020801116%2FFunctions%2F126356644) | [jvm]<br>abstract fun [retry](../-list-all-channel-group/index.md#2020801116%2FFunctions%2F126356644)() |
| [silentCancel](../-list-all-channel-group/index.md#-675955969%2FFunctions%2F126356644) | [jvm]<br>abstract fun [silentCancel](../-list-all-channel-group/index.md#-675955969%2FFunctions%2F126356644)() |
| [sync](../-list-all-channel-group/index.md#40193115%2FFunctions%2F126356644) | [jvm]<br>abstract fun [sync](../-list-all-channel-group/index.md#40193115%2FFunctions%2F126356644)(): [Output](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
