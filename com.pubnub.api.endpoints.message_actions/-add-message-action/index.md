---
title: AddMessageAction - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.endpoints.message_actions](../index.html) / [AddMessageAction](./index.html)

# AddMessageAction

`class AddMessageAction : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.html)`<`[`EntityEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-envelope/index.html)`<`[`PNMessageAction`](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.html)`>, `[`PNAddMessageActionResult`](../../com.pubnub.api.models.consumer.message_actions/-p-n-add-message-action-result/index.html)`>`

### Constructors

| [&lt;init&gt;](-init-.html) | `AddMessageAction(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.html)`)` |

### Properties

| [channel](channel.html) | `lateinit var channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [messageAction](message-action.html) | `lateinit var messageAction: `[`PNMessageAction`](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.html) |

### Functions

| [createResponse](create-response.html) | `fun createResponse(input: Response<`[`EntityEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-envelope/index.html)`<`[`PNMessageAction`](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.html)`>>): `[`PNAddMessageActionResult`](../../com.pubnub.api.models.consumer.message_actions/-p-n-add-message-action-result/index.html)`?` |
| [doWork](do-work.html) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`EntityEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-envelope/index.html)`<`[`PNMessageAction`](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.html)`>>` |
| [getAffectedChannels](get-affected-channels.html) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [operationType](operation-type.html) | `fun operationType(): PNAddMessageAction` |
| [validateParams](validate-params.html) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

