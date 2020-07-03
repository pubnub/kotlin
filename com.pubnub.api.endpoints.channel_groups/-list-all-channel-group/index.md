---
title: ListAllChannelGroup - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.endpoints.channel_groups](../index.html) / [ListAllChannelGroup](./index.html)

# ListAllChannelGroup

`class ListAllChannelGroup : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.html)`<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.html)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>, `[`PNChannelGroupsListAllResult`](../../com.pubnub.api.models.consumer.channel_group/-p-n-channel-groups-list-all-result/index.html)`>`

### Constructors

| [&lt;init&gt;](-init-.html) | `ListAllChannelGroup(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.html)`)` |

### Functions

| [createResponse](create-response.html) | `fun createResponse(input: Response<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.html)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>>): `[`PNChannelGroupsListAllResult`](../../com.pubnub.api.models.consumer.channel_group/-p-n-channel-groups-list-all-result/index.html)`?` |
| [doWork](do-work.html) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.html)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>>` |
| [operationType](operation-type.html) | `fun operationType(): PNChannelGroupsOperation` |

