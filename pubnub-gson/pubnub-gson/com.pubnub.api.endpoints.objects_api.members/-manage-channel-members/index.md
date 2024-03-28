//[pubnub-gson](../../../index.md)/[com.pubnub.api.endpoints.objects_api.members](../index.md)/[ManageChannelMembers](index.md)

# ManageChannelMembers

[jvm]\
interface [ManageChannelMembers](index.md) : [Endpoint](../../com.pubnub.api.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.endpoints/-endpoint/index.md)&gt;

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [jvm]<br>interface [Builder](-builder/index.md) : [BuilderSteps.ChannelStep](../../com.pubnub.api.endpoints/-builder-steps/-channel-step/index.md)&lt;[T](../../com.pubnub.api.endpoints/-builder-steps/-channel-step/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [async](../../com.pubnub.api.endpoints.files/-download-file/index.md#1418965989%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [async](../../com.pubnub.api.endpoints.files/-download-file/index.md#1418965989%2FFunctions%2F-395131529)(p: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [filter](filter.md) | [jvm]<br>abstract fun [filter](filter.md)(filter: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [ManageChannelMembers](index.md) |
| [includeCustom](include-custom.md) | [jvm]<br>abstract fun [includeCustom](include-custom.md)(includeCustom: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [ManageChannelMembers](index.md) |
| [includeTotalCount](include-total-count.md) | [jvm]<br>abstract fun [includeTotalCount](include-total-count.md)(includeTotalCount: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [ManageChannelMembers](index.md) |
| [includeUUID](include-u-u-i-d.md) | [jvm]<br>abstract fun [includeUUID](include-u-u-i-d.md)(includeUUID: [Include.PNUUIDDetailsLevel](../../com.pubnub.api.endpoints.objects_api.utils/-include/-p-n-u-u-i-d-details-level/index.md)): [ManageChannelMembers](index.md) |
| [limit](limit.md) | [jvm]<br>abstract fun [limit](limit.md)(limit: [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)): [ManageChannelMembers](index.md) |
| [operationType](../../com.pubnub.api.endpoints.files/-download-file/index.md#1414065386%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.files/-download-file/index.md#1414065386%2FFunctions%2F-395131529)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [page](page.md) | [jvm]<br>abstract fun [page](page.md)(page: [PNPage](../../../../pubnub-kotlin/com.pubnub.api.models.consumer.objects/-p-n-page/index.md)): [ManageChannelMembers](index.md) |
| [retry](../../com.pubnub.api.endpoints.files/-download-file/index.md#2020801116%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.files/-download-file/index.md#2020801116%2FFunctions%2F-395131529)() |
| [silentCancel](../../com.pubnub.api.endpoints.files/-download-file/index.md#-675955969%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.files/-download-file/index.md#-675955969%2FFunctions%2F-395131529)() |
| [sort](sort.md) | [jvm]<br>abstract fun [sort](sort.md)(sort: [Collection](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html)&lt;[PNSortKey](../../com.pubnub.api.endpoints.objects_api.utils/-p-n-sort-key/index.md)&gt;): [ManageChannelMembers](index.md) |
| [sync](../../com.pubnub.api.endpoints.files/-download-file/index.md#40193115%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.files/-download-file/index.md#40193115%2FFunctions%2F-395131529)(): [Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
