//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints.access](../index.md)/[GrantToken](index.md)

# GrantToken

[jvm]\
interface [GrantToken](index.md) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNGrantTokenResult](../../../../pubnub-gson/com.pubnub.api.models.consumer.access_manager.v3/-p-n-grant-token-result/index.md)&gt;

## Properties

| Name | Summary |
|---|---|
| [ttl](ttl.md) | [jvm]<br>abstract val [ttl](ttl.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#1428680420%2FFunctions%2F51989805) | [jvm]<br>abstract fun [async](index.md#1428680420%2FFunctions%2F51989805)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[PNGrantTokenResult](../../../../pubnub-gson/com.pubnub.api.models.consumer.access_manager.v3/-p-n-grant-token-result/index.md)&gt;&gt;) |
| [operationType](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#1414065386%2FFunctions%2F51989805) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#1414065386%2FFunctions%2F51989805)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#2020801116%2FFunctions%2F51989805) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#2020801116%2FFunctions%2F51989805)() |
| [silentCancel](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#-675955969%2FFunctions%2F51989805) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#-675955969%2FFunctions%2F51989805)() |
| [sync](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#40193115%2FFunctions%2F51989805) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#40193115%2FFunctions%2F51989805)(): [PNGrantTokenResult](../../../../pubnub-gson/com.pubnub.api.models.consumer.access_manager.v3/-p-n-grant-token-result/index.md) |
