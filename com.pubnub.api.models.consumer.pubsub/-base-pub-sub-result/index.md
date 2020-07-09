[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.pubsub](../index.md) / [BasePubSubResult](./index.md)

# BasePubSubResult

`open class BasePubSubResult`

### Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | The channel a PubNub API operation is related to.`val channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [publisher](publisher.md) | The publisher of the PubNub API operation in question.`val publisher: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [subscription](subscription.md) | The subscription a PubNub API operation is related to.`val subscription: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [timetoken](timetoken.md) | Timetoken of the PubNub API operation in question.`val timetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
| [userMetadata](user-metadata.md) | User metadata if any.`val userMetadata: JsonElement?` |

### Inheritors

| Name | Summary |
|---|---|
| [MessageResult](../-message-result/index.md) | `open class MessageResult : `[`BasePubSubResult`](./index.md) |
| [ObjectResult](../../com.pubnub.api.models.consumer.pubsub.objects/-object-result/index.md) | `open class ObjectResult<T> : `[`BasePubSubResult`](./index.md) |
