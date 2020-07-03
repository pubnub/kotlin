---
title: Subscribe - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.endpoints.pubsub](../index.html) / [Subscribe](./index.html)

# Subscribe

`class Subscribe : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.html)`<`[`SubscribeEnvelope`](../../com.pubnub.api.models.server/-subscribe-envelope/index.html)`, `[`SubscribeEnvelope`](../../com.pubnub.api.models.server/-subscribe-envelope/index.html)`>`

### Properties

| [channelGroups](channel-groups.html) | `var channelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [channels](channels.html) | `var channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [filterExpression](filter-expression.html) | `var filterExpression: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [region](region.html) | `var region: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [state](state.html) | `var state: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |
| [timetoken](timetoken.html) | `var timetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |

### Functions

| [createResponse](create-response.html) | `fun createResponse(input: Response<`[`SubscribeEnvelope`](../../com.pubnub.api.models.server/-subscribe-envelope/index.html)`>): `[`SubscribeEnvelope`](../../com.pubnub.api.models.server/-subscribe-envelope/index.html)`?` |
| [doWork](do-work.html) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`SubscribeEnvelope`](../../com.pubnub.api.models.server/-subscribe-envelope/index.html)`>` |
| [getAffectedChannelGroups](get-affected-channel-groups.html) | `fun getAffectedChannelGroups(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [getAffectedChannels](get-affected-channels.html) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [operationType](operation-type.html) | `fun operationType(): PNSubscribeOperation` |
| [validateParams](validate-params.html) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

