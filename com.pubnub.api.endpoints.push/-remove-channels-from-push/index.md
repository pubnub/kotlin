---
title: RemoveChannelsFromPush - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.endpoints.push](../index.html) / [RemoveChannelsFromPush](./index.html)

# RemoveChannelsFromPush

`class RemoveChannelsFromPush : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.html)`<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`, `[`PNPushRemoveChannelResult`](../../com.pubnub.api.models.consumer.push/-p-n-push-remove-channel-result/index.html)`>`

### Constructors

| [&lt;init&gt;](-init-.html) | `RemoveChannelsFromPush(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.html)`)` |

### Properties

| [channels](channels.html) | `lateinit var channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [deviceId](device-id.html) | `lateinit var deviceId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [environment](environment.html) | `var environment: `[`PNPushEnvironment`](../../com.pubnub.api.enums/-p-n-push-environment/index.html) |
| [pushType](push-type.html) | `lateinit var pushType: `[`PNPushType`](../../com.pubnub.api.enums/-p-n-push-type/index.html) |
| [topic](topic.html) | `lateinit var topic: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| [createResponse](create-response.html) | `fun createResponse(input: Response<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`>): `[`PNPushRemoveChannelResult`](../../com.pubnub.api.models.consumer.push/-p-n-push-remove-channel-result/index.html)`?` |
| [doWork](do-work.html) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`>` |
| [getAffectedChannels](get-affected-channels.html) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [operationType](operation-type.html) | `fun operationType(): PNRemovePushNotificationsFromChannelsOperation` |
| [validateParams](validate-params.html) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

