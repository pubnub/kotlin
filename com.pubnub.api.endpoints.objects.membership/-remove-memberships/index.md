[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.objects.membership](../index.md) / [RemoveMemberships](./index.md)

# RemoveMemberships

`class RemoveMemberships : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`EntityArrayEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-array-envelope/index.md)`<`[`PNChannelMembership`](../../com.pubnub.api.models.consumer.objects.membership/-p-n-channel-membership/index.md)`>, `[`PNChannelMembershipArrayResult`](../../com.pubnub.api.models.consumer.objects.membership/-p-n-channel-membership-array-result/index.md)`>`

**See Also**

[PubNub.removeMemberships](#)

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`EntityArrayEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-array-envelope/index.md)`<`[`PNChannelMembership`](../../com.pubnub.api.models.consumer.objects.membership/-p-n-channel-membership/index.md)`>>): `[`PNChannelMembershipArrayResult`](../../com.pubnub.api.models.consumer.objects.membership/-p-n-channel-membership-array-result/index.md)`?` |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`EntityArrayEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-array-envelope/index.md)`<`[`PNChannelMembership`](../../com.pubnub.api.models.consumer.objects.membership/-p-n-channel-membership/index.md)`>>` |
| [operationType](operation-type.md) | `fun operationType(): `[`PNOperationType`](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
