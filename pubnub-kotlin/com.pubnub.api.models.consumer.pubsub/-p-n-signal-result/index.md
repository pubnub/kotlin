//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.pubsub](../index.md)/[PNSignalResult](index.md)

# PNSignalResult

[jvm]\
data class [PNSignalResult](index.md) : [MessageResult](../-message-result/index.md), [PubSubResult](../-pub-sub-result/index.md)

Wrapper around a signal received in [SubscribeCallback.signal](../../com.pubnub.api.callbacks/-subscribe-callback/signal.md).

## Properties

| Name | Summary |
|---|---|
| [channel](../-pub-sub-result/channel.md) | [jvm]<br>open override val [channel](../-pub-sub-result/channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [message](message.md) | [jvm]<br>open override val [message](message.md): JsonElement<br>The actual message content |
| [publisher](../-pub-sub-result/publisher.md) | [jvm]<br>open override val [publisher](../-pub-sub-result/publisher.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [subscription](../-pub-sub-result/subscription.md) | [jvm]<br>open override val [subscription](../-pub-sub-result/subscription.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [timetoken](../-pub-sub-result/timetoken.md) | [jvm]<br>open override val [timetoken](../-pub-sub-result/timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? |
| [userMetadata](../-pub-sub-result/user-metadata.md) | [jvm]<br>open override val [userMetadata](../-pub-sub-result/user-metadata.md): JsonElement? |
