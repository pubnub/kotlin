//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.endpoints.access.builder](../index.md)/[GrantTokenEntitiesBuilder](index.md)

# GrantTokenEntitiesBuilder

[jvm]\
interface [GrantTokenEntitiesBuilder](index.md) : [Endpoint](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [async](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#1418965989%2FFunctions%2F126356644) | [jvm]<br>abstract fun [async](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#1418965989%2FFunctions%2F126356644)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;Output&gt;&gt;) |
| [authorizedUserId](authorized-user-id.md) | [jvm]<br>abstract fun [authorizedUserId](authorized-user-id.md)(userId: [UserId](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-user-id/index.md)): [GrantTokenEntitiesBuilder](index.md) |
| [meta](meta.md) | [jvm]<br>abstract fun [meta](meta.md)(meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [GrantTokenEntitiesBuilder](index.md) |
| [operationType](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#1414065386%2FFunctions%2F126356644) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#1414065386%2FFunctions%2F126356644)(): [PNOperationType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#424483198%2FFunctions%2F126356644) | [jvm]<br>abstract fun [overrideConfiguration](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#424483198%2FFunctions%2F126356644)(configuration: [PNConfiguration](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&gt;<br>Allows to override certain configuration options (see [com.pubnub.api.v2.PNConfigurationOverride.Builder](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only. |
| [retry](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#2020801116%2FFunctions%2F126356644) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#2020801116%2FFunctions%2F126356644)() |
| [silentCancel](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#-675955969%2FFunctions%2F126356644) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#-675955969%2FFunctions%2F126356644)() |
| [spacesPermissions](spaces-permissions.md) | [jvm]<br>abstract fun [spacesPermissions](spaces-permissions.md)(spacesPermissions: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[SpacePermissions](../../com.pubnub.api.java.models.consumer.access_manager.sum/-space-permissions/index.md)&gt;): [GrantTokenEntitiesBuilder](index.md) |
| [sync](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#40193115%2FFunctions%2F126356644) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#40193115%2FFunctions%2F126356644)(): Output |
| [usersPermissions](users-permissions.md) | [jvm]<br>abstract fun [usersPermissions](users-permissions.md)(usersPermissions: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[UserPermissions](../../com.pubnub.api.java.models.consumer.access_manager.sum/-user-permissions/index.md)&gt;): [GrantTokenEntitiesBuilder](index.md) |
