//[pubnub-gson](../../../index.md)/[com.pubnub.api.endpoints.access](../index.md)/[GrantToken](index.md)

# GrantToken

[jvm]\
interface [GrantToken](index.md) : [Endpoint](../../com.pubnub.api.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.endpoints/-endpoint/index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [async](../../com.pubnub.api.endpoints.files/-download-file/index.md#1418965989%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [async](../../com.pubnub.api.endpoints.files/-download-file/index.md#1418965989%2FFunctions%2F-395131529)(p: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [authorizedUUID](authorized-u-u-i-d.md) | [jvm]<br>abstract fun [authorizedUUID](authorized-u-u-i-d.md)(authorizedUUID: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [GrantToken](index.md) |
| [channelGroups](channel-groups.md) | [jvm]<br>abstract fun [channelGroups](channel-groups.md)(channelGroups: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[ChannelGroupGrant](../../com.pubnub.api.models.consumer.access_manager.v3/-channel-group-grant/index.md)&gt;): [GrantToken](index.md) |
| [channels](channels.md) | [jvm]<br>abstract fun [channels](channels.md)(channels: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[ChannelGrant](../../com.pubnub.api.models.consumer.access_manager.v3/-channel-grant/index.md)&gt;): [GrantToken](index.md) |
| [meta](meta.md) | [jvm]<br>abstract fun [meta](meta.md)(meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [GrantToken](index.md) |
| [operationType](../../com.pubnub.api.endpoints.files/-download-file/index.md#1414065386%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.files/-download-file/index.md#1414065386%2FFunctions%2F-395131529)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](../../com.pubnub.api.endpoints.files/-download-file/index.md#2020801116%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.files/-download-file/index.md#2020801116%2FFunctions%2F-395131529)() |
| [silentCancel](../../com.pubnub.api.endpoints.files/-download-file/index.md#-675955969%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.files/-download-file/index.md#-675955969%2FFunctions%2F-395131529)() |
| [sync](../../com.pubnub.api.endpoints.files/-download-file/index.md#40193115%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.files/-download-file/index.md#40193115%2FFunctions%2F-395131529)(): [Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
| [ttl](ttl.md) | [jvm]<br>abstract fun [ttl](ttl.md)(ttl: [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)): [GrantToken](index.md) |
| [uuids](uuids.md) | [jvm]<br>abstract fun [uuids](uuids.md)(uuids: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[UUIDGrant](../../com.pubnub.api.models.consumer.access_manager.v3/-u-u-i-d-grant/index.md)&gt;): [GrantToken](index.md) |
