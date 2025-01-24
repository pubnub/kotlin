//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.models.consumer.pubsub](../index.md)/[MessageResult](index.md)

# MessageResult

interface [MessageResult](index.md) : [PubSubResult](../-pub-sub-result/index.md)

#### Inheritors

| |
|---|
| [PNMessageResult](../-p-n-message-result/index.md) |
| [PNSignalResult](../-p-n-signal-result/index.md) |

## Properties

| Name | Summary |
|---|---|
| [channel](../-pub-sub-result/channel.md) | [common]<br>abstract override val [channel](../-pub-sub-result/channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [customMessageType](custom-message-type.md) | [common]<br>abstract val [customMessageType](custom-message-type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? |
| [message](message.md) | [common]<br>abstract val [message](message.md): [JsonElement](../../com.pubnub.api/-json-element/index.md)<br>The actual message content |
| [publisher](../-pub-sub-result/publisher.md) | [common]<br>abstract val [publisher](../-pub-sub-result/publisher.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? |
| [subscription](../-pub-sub-result/subscription.md) | [common]<br>abstract override val [subscription](../-pub-sub-result/subscription.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? |
| [timetoken](../-pub-sub-result/timetoken.md) | [common]<br>abstract override val [timetoken](../-pub-sub-result/timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)? |
| [userMetadata](../-pub-sub-result/user-metadata.md) | [common]<br>abstract val [userMetadata](../-pub-sub-result/user-metadata.md): [JsonElement](../../com.pubnub.api/-json-element/index.md)? |
