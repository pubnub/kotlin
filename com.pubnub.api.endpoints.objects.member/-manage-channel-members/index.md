[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.objects.member](../index.md) / [ManageChannelMembers](./index.md)

# ManageChannelMembers

`class ManageChannelMembers : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`EntityArrayEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-array-envelope/index.md)`<`[`PNMember`](../../com.pubnub.api.models.consumer.objects.member/-p-n-member/index.md)`>, `[`PNMemberArrayResult`](../../com.pubnub.api.models.consumer.objects.member/-p-n-member-array-result/index.md)`>`

**See Also**

[PubNub.manageChannelMembers](../../com.pubnub.api/-pub-nub/manage-channel-members.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ManageChannelMembers(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`, uuidsToSet: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`MemberInput`](../../com.pubnub.api.models.consumer.objects.member/-member-input/index.md)`>, uuidsToRemove: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, collectionQueryParameters: `[`CollectionQueryParameters`](../../com.pubnub.api.endpoints.objects.internal/-collection-query-parameters/index.md)`, includeQueryParam: `[`IncludeQueryParam`](../../com.pubnub.api.endpoints.objects.internal/-include-query-param/index.md)`)` |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`EntityArrayEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-array-envelope/index.md)`<`[`PNMember`](../../com.pubnub.api.models.consumer.objects.member/-p-n-member/index.md)`>>): `[`PNMemberArrayResult`](../../com.pubnub.api.models.consumer.objects.member/-p-n-member-array-result/index.md)`?` |
| [doWork](do-work.md) | `fun doWork(queryParams: <ERROR CLASS><`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`EntityArrayEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-array-envelope/index.md)`<`[`PNMember`](../../com.pubnub.api.models.consumer.objects.member/-p-n-member/index.md)`>>` |
| [operationType](operation-type.md) | `fun operationType(): `[`PNOperationType`](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
