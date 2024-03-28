//[pubnub-core-api](../../../index.md)/[com.pubnub.api.models.consumer.pubsub](../index.md)/[PNMessageResult](index.md)

# PNMessageResult

[jvm]\
class [PNMessageResult](index.md)(basePubSubResult: [PubSubResult](../-pub-sub-result/index.md), val message: JsonElement, val error: [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null) : [MessageResult](../-message-result/index.md), [PubSubResult](../-pub-sub-result/index.md)

Wrapper around an actual message.

## Constructors

| | |
|---|---|
| [PNMessageResult](-p-n-message-result.md) | [jvm]<br>constructor(basePubSubResult: [PubSubResult](../-pub-sub-result/index.md), message: JsonElement, error: [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null) |

## Properties

| Name | Summary |
|---|---|
| [channel](../-pub-sub-result/channel.md) | [jvm]<br>open override val [channel](../-pub-sub-result/channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [error](error.md) | [jvm]<br>val [error](error.md): [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null |
| [message](message.md) | [jvm]<br>open override val [message](message.md): JsonElement<br>The actual message content |
| [publisher](../-pub-sub-result/publisher.md) | [jvm]<br>open override val [publisher](../-pub-sub-result/publisher.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [subscription](../-pub-sub-result/subscription.md) | [jvm]<br>open override val [subscription](../-pub-sub-result/subscription.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [timetoken](../-pub-sub-result/timetoken.md) | [jvm]<br>open override val [timetoken](../-pub-sub-result/timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? |
| [userMetadata](../-pub-sub-result/user-metadata.md) | [jvm]<br>open override val [userMetadata](../-pub-sub-result/user-metadata.md): JsonElement? |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [jvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [jvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
