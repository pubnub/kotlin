//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.endpoints.access.builder](../index.md)/[GrantTokenObjectsBuilder](index.md)

# GrantTokenObjectsBuilder

[jvm]\
interface [GrantTokenObjectsBuilder](index.md) : [Endpoint](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [async](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md#1418965989%2FFunctions%2F126356644) | [jvm]<br>abstract fun [async](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md#1418965989%2FFunctions%2F126356644)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;Output&gt;&gt;) |
| [authorizedUUID](authorized-u-u-i-d.md) | [jvm]<br>abstract fun [authorizedUUID](authorized-u-u-i-d.md)(authorizedUUID: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [GrantTokenObjectsBuilder](index.md) |
| [channelGroups](channel-groups.md) | [jvm]<br>abstract fun [channelGroups](channel-groups.md)(channelGroups: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[ChannelGroupGrant](../../com.pubnub.api.java.models.consumer.access_manager.v3/-channel-group-grant/index.md)&gt;): [GrantTokenObjectsBuilder](index.md) |
| [channels](channels.md) | [jvm]<br>abstract fun [channels](channels.md)(channels: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[ChannelGrant](../../com.pubnub.api.java.models.consumer.access_manager.v3/-channel-grant/index.md)&gt;): [GrantTokenObjectsBuilder](index.md) |
| [meta](meta.md) | [jvm]<br>abstract fun [meta](meta.md)(meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [GrantTokenObjectsBuilder](index.md) |
| [operationType](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md#1414065386%2FFunctions%2F126356644) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md#1414065386%2FFunctions%2F126356644)(): [PNOperationType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md#424483198%2FFunctions%2F126356644) | [jvm]<br>abstract fun [overrideConfiguration](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md#424483198%2FFunctions%2F126356644)(configuration: [PNConfiguration](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&gt;<br>Allows to override certain configuration options (see [com.pubnub.api.v2.PNConfigurationOverride.Builder](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only. |
| [retry](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md#2020801116%2FFunctions%2F126356644) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md#2020801116%2FFunctions%2F126356644)() |
| [silentCancel](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md#-675955969%2FFunctions%2F126356644) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md#-675955969%2FFunctions%2F126356644)() |
| [sync](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md#40193115%2FFunctions%2F126356644) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md#40193115%2FFunctions%2F126356644)(): Output |
| [ttl](ttl.md) | [jvm]<br>abstract fun [ttl](ttl.md)(ttl: [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)): [GrantTokenObjectsBuilder](index.md) |
| [uuids](uuids.md) | [jvm]<br>abstract fun [uuids](uuids.md)(uuids: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[UUIDGrant](../../com.pubnub.api.java.models.consumer.access_manager.v3/-u-u-i-d-grant/index.md)&gt;): [GrantTokenObjectsBuilder](index.md) |
