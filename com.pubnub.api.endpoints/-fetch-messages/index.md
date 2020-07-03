---
title: FetchMessages - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.endpoints](../index.html) / [FetchMessages](./index.html)

# FetchMessages

`class FetchMessages : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.html)`<`[`FetchMessagesEnvelope`](../../com.pubnub.api.models.server/-fetch-messages-envelope/index.html)`, `[`PNFetchMessagesResult`](../../com.pubnub.api.models.consumer.history/-p-n-fetch-messages-result/index.html)`>`

### Constructors

| [&lt;init&gt;](-init-.html) | `FetchMessages(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.html)`)` |

### Properties

| [channels](channels.html) | `lateinit var channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [end](end.html) | `var end: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
| [includeMessageActions](include-message-actions.html) | `var includeMessageActions: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeMeta](include-meta.html) | `var includeMeta: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [maximumPerChannel](maximum-per-channel.html) | `var maximumPerChannel: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [start](start.html) | `var start: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |

### Functions

| [createResponse](create-response.html) | `fun createResponse(input: Response<`[`FetchMessagesEnvelope`](../../com.pubnub.api.models.server/-fetch-messages-envelope/index.html)`>): `[`PNFetchMessagesResult`](../../com.pubnub.api.models.consumer.history/-p-n-fetch-messages-result/index.html)`?` |
| [doWork](do-work.html) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`FetchMessagesEnvelope`](../../com.pubnub.api.models.server/-fetch-messages-envelope/index.html)`>` |
| [getAffectedChannels](get-affected-channels.html) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [operationType](operation-type.html) | `fun operationType(): PNFetchMessagesOperation` |
| [validateParams](validate-params.html) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

