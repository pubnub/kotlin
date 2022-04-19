[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.channel_groups](../index.md) / [AllChannelsChannelGroup](./index.md)

# AllChannelsChannelGroup

`class AllChannelsChannelGroup : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>, `[`PNChannelGroupsAllChannelsResult`](../../com.pubnub.api.models.consumer.channel_group/-p-n-channel-groups-all-channels-result/index.md)`>`

**See Also**

[PubNub.listChannelsForChannelGroup](../../com.pubnub.api/-pub-nub/list-channels-for-channel-group.md)

### Properties

| Name | Summary |
|---|---|
| [channelGroup](channel-group.md) | `val channelGroup: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>>): `[`PNChannelGroupsAllChannelsResult`](../../com.pubnub.api.models.consumer.channel_group/-p-n-channel-groups-all-channels-result/index.md) |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>>` |
| [getAffectedChannelGroups](get-affected-channel-groups.md) | `fun getAffectedChannelGroups(): <ERROR CLASS>` |
| [operationType](operation-type.md) | `fun operationType(): PNChannelsForGroupOperation` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
