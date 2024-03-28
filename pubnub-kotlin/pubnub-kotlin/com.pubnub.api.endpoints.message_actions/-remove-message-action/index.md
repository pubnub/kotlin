//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints.message_actions](../index.md)/[RemoveMessageAction](index.md)

# RemoveMessageAction

interface [RemoveMessageAction](index.md) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNRemoveMessageActionResult](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.message_actions/-p-n-remove-message-action-result/index.md)&gt; 

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
| [async](index.md#-1890068926%2FFunctions%2F51989805) | [jvm]<br>abstract fun [async](index.md#-1890068926%2FFunctions%2F51989805)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[PNRemoveMessageActionResult](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.message_actions/-p-n-remove-message-action-result/index.md)&gt;&gt;) |
| [operationType](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#1414065386%2FFunctions%2F51989805) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#1414065386%2FFunctions%2F51989805)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#2020801116%2FFunctions%2F51989805) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#2020801116%2FFunctions%2F51989805)() |
| [silentCancel](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#-675955969%2FFunctions%2F51989805) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#-675955969%2FFunctions%2F51989805)() |
| [sync](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#40193115%2FFunctions%2F51989805) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#40193115%2FFunctions%2F51989805)(): [PNRemoveMessageActionResult](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.message_actions/-p-n-remove-message-action-result/index.md) |
