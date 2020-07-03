---
title: DeleteMessages - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.endpoints](../index.html) / [DeleteMessages](./index.html)

# DeleteMessages

`class DeleteMessages : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.html)`<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`, `[`PNDeleteMessagesResult`](../../com.pubnub.api.models.consumer.history/-p-n-delete-messages-result/index.html)`>`

### Constructors

| [&lt;init&gt;](-init-.html) | `DeleteMessages(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.html)`)` |

### Properties

| [channels](channels.html) | `lateinit var channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [end](end.html) | `var end: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
| [start](start.html) | `var start: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |

### Functions

| [createResponse](create-response.html) | `fun createResponse(input: Response<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`>): `[`PNDeleteMessagesResult`](../../com.pubnub.api.models.consumer.history/-p-n-delete-messages-result/index.html)`?` |
| [doWork](do-work.html) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`>` |
| [operationType](operation-type.html) | `fun operationType(): PNDeleteMessagesOperation` |
| [validateParams](validate-params.html) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

