//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.endpoints.message_actions](../index.md)/[RemoveMessageAction](index.md)

# RemoveMessageAction

interface [RemoveMessageAction](index.md) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNRemoveMessageActionResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.message_actions/-p-n-remove-message-action-result/index.md)&gt; 

#### See also

| |
|---|
| PubNub.removeMessageAction |

## Properties

| Name | Summary |
|---|---|
| [actionTimetoken](action-timetoken.md) | [jvm]<br>abstract val [actionTimetoken](action-timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [channel](channel.md) | [jvm]<br>abstract val [channel](channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [messageTimetoken](message-timetoken.md) | [jvm]<br>abstract val [messageTimetoken](message-timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#-1890068926%2FFunctions%2F1262999440) | [jvm]<br>abstract fun [async](index.md#-1890068926%2FFunctions%2F1262999440)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[PNRemoveMessageActionResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.message_actions/-p-n-remove-message-action-result/index.md)&gt;&gt;) |
| [operationType](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#1414065386%2FFunctions%2F1262999440) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#1414065386%2FFunctions%2F1262999440)(): [PNOperationType](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](../../com.pubnub.api/-endpoint/override-configuration.md) | [jvm]<br>abstract fun [overrideConfiguration](../../com.pubnub.api/-endpoint/override-configuration.md)(configuration: [PNConfiguration](../../com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNRemoveMessageActionResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.message_actions/-p-n-remove-message-action-result/index.md)&gt;<br>abstract fun [overrideConfiguration](../../com.pubnub.api/-endpoint/override-configuration.md)(action: [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNRemoveMessageActionResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.message_actions/-p-n-remove-message-action-result/index.md)&gt;<br>Allows to override certain configuration options (see [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only. |
| [retry](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#2020801116%2FFunctions%2F1262999440) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#2020801116%2FFunctions%2F1262999440)() |
| [silentCancel](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#-675955969%2FFunctions%2F1262999440) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#-675955969%2FFunctions%2F1262999440)() |
| [sync](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#40193115%2FFunctions%2F1262999440) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#40193115%2FFunctions%2F1262999440)(): [PNRemoveMessageActionResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.message_actions/-p-n-remove-message-action-result/index.md) |
