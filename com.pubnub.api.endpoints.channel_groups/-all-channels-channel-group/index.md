---
title: AllChannelsChannelGroup - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.endpoints.channel_groups](../index.html) / [AllChannelsChannelGroup](./index.html)

# AllChannelsChannelGroup

`class AllChannelsChannelGroup : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.html)`<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.html)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>, `[`PNChannelGroupsAllChannelsResult`](../../com.pubnub.api.models.consumer.channel_group/-p-n-channel-groups-all-channels-result/index.html)`>`

### Constructors

| [&lt;init&gt;](-init-.html) | `AllChannelsChannelGroup(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.html)`)` |

### Properties

| [channelGroup](channel-group.html) | `lateinit var channelGroup: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| [createResponse](create-response.html) | `fun createResponse(input: Response<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.html)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>>): `[`PNChannelGroupsAllChannelsResult`](../../com.pubnub.api.models.consumer.channel_group/-p-n-channel-groups-all-channels-result/index.html)`?` |
| [doWork](do-work.html) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.html)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>>` |
| [getAffectedChannelGroups](get-affected-channel-groups.html) | `fun getAffectedChannelGroups(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [operationType](operation-type.html) | `fun operationType(): PNChannelsForGroupOperation` |
| [validateParams](validate-params.html) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

