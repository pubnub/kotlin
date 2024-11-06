//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.models.consumer.pubsub](../index.md)/[BasePubSubResult](index.md)

# BasePubSubResult

[common]\
data class [BasePubSubResult](index.md)(val channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val subscription: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, val timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)?, val userMetadata: [JsonElement](../../com.pubnub.api/-json-element/index.md)?, val publisher: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?) : [PubSubResult](../-pub-sub-result/index.md)

## Constructors

| | |
|---|---|
| [BasePubSubResult](-base-pub-sub-result.md) | [common]<br>constructor(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), subscription: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)?, userMetadata: [JsonElement](../../com.pubnub.api/-json-element/index.md)?, publisher: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?) |

## Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | [common]<br>open override val [channel](channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The channel a PubNub API operation is related to. |
| [publisher](publisher.md) | [common]<br>open override val [publisher](publisher.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>The publisher of the PubNub API operation in question. |
| [subscription](subscription.md) | [common]<br>open override val [subscription](subscription.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>The subscriptions a PubNub API operation is related to. |
| [timetoken](timetoken.md) | [common]<br>open override val [timetoken](timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)?<br>Timetoken of the PubNub API operation in question. |
| [userMetadata](user-metadata.md) | [common]<br>open override val [userMetadata](user-metadata.md): [JsonElement](../../com.pubnub.api/-json-element/index.md)?<br>User metadata if any. |
