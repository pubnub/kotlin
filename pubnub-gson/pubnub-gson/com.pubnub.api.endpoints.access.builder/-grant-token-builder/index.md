//[pubnub-gson](../../../index.md)/[com.pubnub.api.endpoints.access.builder](../index.md)/[GrantTokenBuilder](index.md)

# GrantTokenBuilder

[jvm]\
open class [GrantTokenBuilder](index.md) : [AbstractGrantTokenBuilder](../-abstract-grant-token-builder/index.md)&lt;[T](../-abstract-grant-token-builder/index.md)&gt;

## Constructors

| | |
|---|---|
| [GrantTokenBuilder](-grant-token-builder.md) | [jvm]<br>constructor(pubnub: PubNubCore, grantToken: [GrantToken](../../com.pubnub.api.endpoints.access/-grant-token/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [async](../../com.pubnub.api.endpoints.files/-download-file/index.md#1418965989%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [async](../../com.pubnub.api.endpoints.files/-download-file/index.md#1418965989%2FFunctions%2F-395131529)(p: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;)<br>open fun [async](../-grant-token-entities-builder/index.md#-1510486822%2FFunctions%2F-395131529)(@NotNullcallback: @NotNull[Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;T&gt;&gt;) |
| [authorizedUserId](authorized-user-id.md) | [jvm]<br>open fun [authorizedUserId](authorized-user-id.md)(userId: [UserId](../../../../pubnub-gson/com.pubnub.api/-user-id/index.md)): [GrantTokenEntitiesBuilder](../-grant-token-entities-builder/index.md) |
| [authorizedUUID](authorized-u-u-i-d.md) | [jvm]<br>open fun [authorizedUUID](authorized-u-u-i-d.md)(authorizedUUID: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [GrantTokenObjectsBuilder](../-grant-token-objects-builder/index.md) |
| [channelGroups](channel-groups.md) | [jvm]<br>open fun [channelGroups](channel-groups.md)(channelGroups: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[ChannelGroupGrant](../../com.pubnub.api.models.consumer.access_manager.v3/-channel-group-grant/index.md)&gt;): [GrantTokenObjectsBuilder](../-grant-token-objects-builder/index.md) |
| [channels](channels.md) | [jvm]<br>open fun [channels](channels.md)(channels: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[ChannelGrant](../../com.pubnub.api.models.consumer.access_manager.v3/-channel-grant/index.md)&gt;): [GrantTokenObjectsBuilder](../-grant-token-objects-builder/index.md) |
| [getOperationType](../-grant-token-entities-builder/index.md#1720561945%2FFunctions%2F-395131529) | [jvm]<br>open fun [getOperationType](../-grant-token-entities-builder/index.md#1720561945%2FFunctions%2F-395131529)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [meta](meta.md) | [jvm]<br>open fun [meta](meta.md)(meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [GrantTokenBuilder](index.md) |
| [operationType](../../com.pubnub.api.endpoints.files/-download-file/index.md#1414065386%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.files/-download-file/index.md#1414065386%2FFunctions%2F-395131529)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md)<br>@NotNull<br>open fun [operationType](../-grant-token-entities-builder/index.md#-2136612235%2FFunctions%2F-395131529)(): @NotNull[PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](../../com.pubnub.api.endpoints.files/-download-file/index.md#2020801116%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.files/-download-file/index.md#2020801116%2FFunctions%2F-395131529)()<br>open fun [retry](../-grant-token-entities-builder/index.md#993822702%2FFunctions%2F-395131529)() |
| [silentCancel](../../com.pubnub.api.endpoints.files/-download-file/index.md#-675955969%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.files/-download-file/index.md#-675955969%2FFunctions%2F-395131529)()<br>open fun [silentCancel](../-grant-token-entities-builder/index.md#1057725301%2FFunctions%2F-395131529)() |
| [spacesPermissions](spaces-permissions.md) | [jvm]<br>open fun [spacesPermissions](spaces-permissions.md)(spacesPermissions: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[SpacePermissions](../../com.pubnub.api.models.consumer.access_manager.sum/-space-permissions/index.md)&gt;): [GrantTokenEntitiesBuilder](../-grant-token-entities-builder/index.md) |
| [sync](../../com.pubnub.api.endpoints.files/-download-file/index.md#40193115%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.files/-download-file/index.md#40193115%2FFunctions%2F-395131529)(): [Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)<br>open fun [sync](../-grant-token-entities-builder/index.md#2085274761%2FFunctions%2F-395131529)(): T |
| [ttl](ttl.md) | [jvm]<br>open fun [~~ttl~~](ttl.md)(ttl: [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)): [GrantTokenBuilder](index.md) |
| [usersPermissions](users-permissions.md) | [jvm]<br>open fun [usersPermissions](users-permissions.md)(usersPermissions: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[UserPermissions](../../com.pubnub.api.models.consumer.access_manager.sum/-user-permissions/index.md)&gt;): [GrantTokenEntitiesBuilder](../-grant-token-entities-builder/index.md) |
| [uuids](uuids.md) | [jvm]<br>open fun [uuids](uuids.md)(uuids: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[UUIDGrant](../../com.pubnub.api.models.consumer.access_manager.v3/-u-u-i-d-grant/index.md)&gt;): [GrantTokenObjectsBuilder](../-grant-token-objects-builder/index.md) |
