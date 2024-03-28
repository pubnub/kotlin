//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints](../index.md)/[FetchMessages](index.md)

# FetchMessages

interface [FetchMessages](index.md) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNFetchMessagesResult](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.history/-p-n-fetch-messages-result/index.md)&gt; 

#### See also

| |
|---|
| PubNub.fetchMessages |

## Properties

| Name | Summary |
|---|---|
| [channels](channels.md) | [jvm]<br>abstract val [channels](channels.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [includeMessageActions](include-message-actions.md) | [jvm]<br>abstract val [includeMessageActions](include-message-actions.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeMessageType](include-message-type.md) | [jvm]<br>abstract val [includeMessageType](include-message-type.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeMeta](include-meta.md) | [jvm]<br>abstract val [includeMeta](include-meta.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeUUID](include-u-u-i-d.md) | [jvm]<br>abstract val [includeUUID](include-u-u-i-d.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [page](page.md) | [jvm]<br>abstract val [page](page.md): [PNBoundedPage](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer/-p-n-bounded-page/index.md) |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#1636908806%2FFunctions%2F51989805) | [jvm]<br>abstract fun [async](index.md#1636908806%2FFunctions%2F51989805)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[PNFetchMessagesResult](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.history/-p-n-fetch-messages-result/index.md)&gt;&gt;) |
| [operationType](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#1414065386%2FFunctions%2F51989805) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#1414065386%2FFunctions%2F51989805)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#2020801116%2FFunctions%2F51989805) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#2020801116%2FFunctions%2F51989805)() |
| [silentCancel](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#-675955969%2FFunctions%2F51989805) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#-675955969%2FFunctions%2F51989805)() |
| [sync](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#40193115%2FFunctions%2F51989805) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#40193115%2FFunctions%2F51989805)(): [PNFetchMessagesResult](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.history/-p-n-fetch-messages-result/index.md) |
