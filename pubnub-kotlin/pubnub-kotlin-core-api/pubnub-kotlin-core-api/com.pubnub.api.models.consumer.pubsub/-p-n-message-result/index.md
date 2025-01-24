//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.models.consumer.pubsub](../index.md)/[PNMessageResult](index.md)

# PNMessageResult

[common]\
class [PNMessageResult](index.md)(basePubSubResult: [PubSubResult](../-pub-sub-result/index.md), val message: [JsonElement](../../com.pubnub.api/-json-element/index.md), val customMessageType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null, val error: [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null) : [MessageResult](../-message-result/index.md), [PubSubResult](../-pub-sub-result/index.md)

Wrapper around an actual message.

## Constructors

| | |
|---|---|
| [PNMessageResult](-p-n-message-result.md) | [common]<br>constructor(basePubSubResult: [PubSubResult](../-pub-sub-result/index.md), message: [JsonElement](../../com.pubnub.api/-json-element/index.md), customMessageType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null, error: [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null) |

## Properties

| Name | Summary |
|---|---|
| [channel](../-pub-sub-result/channel.md) | [common]<br>open override val [channel](../-pub-sub-result/channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [customMessageType](custom-message-type.md) | [common]<br>open override val [customMessageType](custom-message-type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null |
| [error](error.md) | [common]<br>val [error](error.md): [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null |
| [message](message.md) | [common]<br>open override val [message](message.md): [JsonElement](../../com.pubnub.api/-json-element/index.md)<br>The actual message content |
| [publisher](../-pub-sub-result/publisher.md) | [common]<br>open override val [publisher](../-pub-sub-result/publisher.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? |
| [subscription](../-pub-sub-result/subscription.md) | [common]<br>open override val [subscription](../-pub-sub-result/subscription.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? |
| [timetoken](../-pub-sub-result/timetoken.md) | [common]<br>open override val [timetoken](../-pub-sub-result/timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)? |
| [userMetadata](../-pub-sub-result/user-metadata.md) | [common]<br>open override val [userMetadata](../-pub-sub-result/user-metadata.md): [JsonElement](../../com.pubnub.api/-json-element/index.md)? |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [common]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [common]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
