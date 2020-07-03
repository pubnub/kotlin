---
title: WhereNow - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.endpoints.presence](../index.html) / [WhereNow](./index.html)

# WhereNow

`class WhereNow : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.html)`<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.html)`<`[`WhereNowPayload`](../../com.pubnub.api.models.server.presence/-where-now-payload/index.html)`>, `[`PNWhereNowResult`](../../com.pubnub.api.models.consumer.presence/-p-n-where-now-result/index.html)`>`

### Constructors

| [&lt;init&gt;](-init-.html) | `WhereNow(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.html)`)` |

### Properties

| [uuid](uuid.html) | `var uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| [createResponse](create-response.html) | `fun createResponse(input: Response<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.html)`<`[`WhereNowPayload`](../../com.pubnub.api.models.server.presence/-where-now-payload/index.html)`>>): `[`PNWhereNowResult`](../../com.pubnub.api.models.consumer.presence/-p-n-where-now-result/index.html)`?` |
| [doWork](do-work.html) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.html)`<`[`WhereNowPayload`](../../com.pubnub.api.models.server.presence/-where-now-payload/index.html)`>>` |
| [operationType](operation-type.html) | `fun operationType(): PNWhereNowOperation` |

