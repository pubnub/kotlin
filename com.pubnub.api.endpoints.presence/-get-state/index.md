---
title: GetState - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.endpoints.presence](../index.html) / [GetState](./index.html)

# GetState

`class GetState : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.html)`<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.html)`<JsonElement>, `[`PNGetStateResult`](../../com.pubnub.api.models.consumer.presence/-p-n-get-state-result/index.html)`>`

### Constructors

| [&lt;init&gt;](-init-.html) | `GetState(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.html)`)` |

### Properties

| [channelGroups](channel-groups.html) | `var channelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [channels](channels.html) | `var channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [uuid](uuid.html) | `var uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| [createResponse](create-response.html) | `fun createResponse(input: Response<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.html)`<JsonElement>>): `[`PNGetStateResult`](../../com.pubnub.api.models.consumer.presence/-p-n-get-state-result/index.html)`?` |
| [doWork](do-work.html) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.html)`<JsonElement>>` |
| [getAffectedChannelGroups](get-affected-channel-groups.html) | `fun getAffectedChannelGroups(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [getAffectedChannels](get-affected-channels.html) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [operationType](operation-type.html) | `fun operationType(): PNGetState` |
| [validateParams](validate-params.html) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

