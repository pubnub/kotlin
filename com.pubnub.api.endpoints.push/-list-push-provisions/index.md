---
title: ListPushProvisions - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.endpoints.push](../index.html) / [ListPushProvisions](./index.html)

# ListPushProvisions

`class ListPushProvisions : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.html)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, `[`PNPushListProvisionsResult`](../../com.pubnub.api.models.consumer.push/-p-n-push-list-provisions-result/index.html)`>`

### Constructors

| [&lt;init&gt;](-init-.html) | `ListPushProvisions(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.html)`)` |

### Properties

| [deviceId](device-id.html) | `lateinit var deviceId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [environment](environment.html) | `var environment: `[`PNPushEnvironment`](../../com.pubnub.api.enums/-p-n-push-environment/index.html) |
| [pushType](push-type.html) | `lateinit var pushType: `[`PNPushType`](../../com.pubnub.api.enums/-p-n-push-type/index.html) |
| [topic](topic.html) | `lateinit var topic: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| [createResponse](create-response.html) | `fun createResponse(input: Response<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>): `[`PNPushListProvisionsResult`](../../com.pubnub.api.models.consumer.push/-p-n-push-list-provisions-result/index.html)`?` |
| [doWork](do-work.html) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>` |
| [operationType](operation-type.html) | `fun operationType(): PNPushNotificationEnabledChannelsOperation` |
| [validateParams](validate-params.html) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

