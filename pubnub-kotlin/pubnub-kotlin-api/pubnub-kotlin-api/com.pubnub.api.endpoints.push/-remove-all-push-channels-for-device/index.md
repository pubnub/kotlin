//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.endpoints.push](../index.md)/[RemoveAllPushChannelsForDevice](index.md)

# RemoveAllPushChannelsForDevice

interface [RemoveAllPushChannelsForDevice](index.md) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNPushRemoveAllChannelsResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.push/-p-n-push-remove-all-channels-result/index.md)&gt; 

#### See also

| |
|---|
| PubNub.removeAllPushNotificationsFromDeviceWithPushToken |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#-1558412781%2FFunctions%2F1262999440) | [jvm]<br>abstract fun [async](index.md#-1558412781%2FFunctions%2F1262999440)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[PNPushRemoveAllChannelsResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.push/-p-n-push-remove-all-channels-result/index.md)&gt;&gt;) |
| [operationType](../-remove-channels-from-push/index.md#1414065386%2FFunctions%2F1262999440) | [jvm]<br>abstract fun [operationType](../-remove-channels-from-push/index.md#1414065386%2FFunctions%2F1262999440)(): [PNOperationType](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](../../com.pubnub.api/-endpoint/override-configuration.md) | [jvm]<br>abstract fun [overrideConfiguration](../../com.pubnub.api/-endpoint/override-configuration.md)(configuration: [PNConfiguration](../../com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNPushRemoveAllChannelsResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.push/-p-n-push-remove-all-channels-result/index.md)&gt;<br>abstract fun [overrideConfiguration](../../com.pubnub.api/-endpoint/override-configuration.md)(action: [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNPushRemoveAllChannelsResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.push/-p-n-push-remove-all-channels-result/index.md)&gt;<br>Allows to override certain configuration options (see [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only. |
| [retry](../-remove-channels-from-push/index.md#2020801116%2FFunctions%2F1262999440) | [jvm]<br>abstract fun [retry](../-remove-channels-from-push/index.md#2020801116%2FFunctions%2F1262999440)() |
| [silentCancel](../-remove-channels-from-push/index.md#-675955969%2FFunctions%2F1262999440) | [jvm]<br>abstract fun [silentCancel](../-remove-channels-from-push/index.md#-675955969%2FFunctions%2F1262999440)() |
| [sync](../-remove-channels-from-push/index.md#40193115%2FFunctions%2F1262999440) | [jvm]<br>abstract fun [sync](../-remove-channels-from-push/index.md#40193115%2FFunctions%2F1262999440)(): [PNPushRemoveAllChannelsResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.push/-p-n-push-remove-all-channels-result/index.md) |
