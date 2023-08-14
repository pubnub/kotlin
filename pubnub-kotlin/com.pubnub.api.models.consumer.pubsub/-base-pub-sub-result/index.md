//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.pubsub](../index.md)/[BasePubSubResult](index.md)

# BasePubSubResult

[jvm]\
data class [BasePubSubResult](index.md)(val channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val subscription: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, val timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)?, val userMetadata: JsonElement?, val publisher: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?) : [PubSubResult](../-pub-sub-result/index.md)

## Constructors

| | |
|---|---|
| [BasePubSubResult](-base-pub-sub-result.md) | [jvm]<br>fun [BasePubSubResult](-base-pub-sub-result.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), subscription: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)?, userMetadata: JsonElement?, publisher: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?) |

## Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | [jvm]<br>open override val [channel](channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The channel a PubNub API operation is related to. |
| [publisher](publisher.md) | [jvm]<br>open override val [publisher](publisher.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>The publisher of the PubNub API operation in question. |
| [subscription](subscription.md) | [jvm]<br>open override val [subscription](subscription.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>The subscription a PubNub API operation is related to. |
| [timetoken](timetoken.md) | [jvm]<br>open override val [timetoken](timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)?<br>Timetoken of the PubNub API operation in question. |
| [userMetadata](user-metadata.md) | [jvm]<br>open override val [userMetadata](user-metadata.md): JsonElement?<br>User metadata if any. |
