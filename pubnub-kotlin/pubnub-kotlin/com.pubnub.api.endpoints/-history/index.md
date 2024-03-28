//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints](../index.md)/[History](index.md)

# History

interface [History](index.md) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNHistoryResult](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.history/-p-n-history-result/index.md)&gt; 

#### See also

| |
|---|
| PubNub.history |

## Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | [jvm]<br>abstract val [channel](channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [count](count.md) | [jvm]<br>abstract val [count](count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [end](end.md) | [jvm]<br>abstract val [end](end.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? |
| [includeMeta](include-meta.md) | [jvm]<br>abstract val [includeMeta](include-meta.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeTimetoken](include-timetoken.md) | [jvm]<br>abstract val [includeTimetoken](include-timetoken.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [reverse](reverse.md) | [jvm]<br>abstract val [reverse](reverse.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [start](start.md) | [jvm]<br>abstract val [start](start.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#-2071998248%2FFunctions%2F51989805) | [jvm]<br>abstract fun [async](index.md#-2071998248%2FFunctions%2F51989805)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[PNHistoryResult](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.history/-p-n-history-result/index.md)&gt;&gt;) |
| [operationType](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#1414065386%2FFunctions%2F51989805) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#1414065386%2FFunctions%2F51989805)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#2020801116%2FFunctions%2F51989805) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#2020801116%2FFunctions%2F51989805)() |
| [silentCancel](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#-675955969%2FFunctions%2F51989805) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#-675955969%2FFunctions%2F51989805)() |
| [sync](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#40193115%2FFunctions%2F51989805) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#40193115%2FFunctions%2F51989805)(): [PNHistoryResult](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.history/-p-n-history-result/index.md) |
