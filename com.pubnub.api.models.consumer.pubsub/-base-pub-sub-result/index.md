[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.pubsub](../index.md) / [BasePubSubResult](./index.md)

# BasePubSubResult

`data class BasePubSubResult : `[`PubSubResult`](../-pub-sub-result/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `BasePubSubResult(channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, subscription: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, timetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?, userMetadata: JsonElement?, publisher: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?)` |

### Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | The channel a PubNub API operation is related to.`val channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [publisher](publisher.md) | The publisher of the PubNub API operation in question.`val publisher: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [subscription](subscription.md) | The subscription a PubNub API operation is related to.`val subscription: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [timetoken](timetoken.md) | Timetoken of the PubNub API operation in question.`val timetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
| [userMetadata](user-metadata.md) | User metadata if any.`val userMetadata: JsonElement?` |
