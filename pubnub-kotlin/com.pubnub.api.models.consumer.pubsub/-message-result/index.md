//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.pubsub](../index.md)/[MessageResult](index.md)

# MessageResult

[jvm]\
interface [MessageResult](index.md) : [PubSubResult](../-pub-sub-result/index.md)

## Properties

| Name | Summary |
|---|---|
| [channel](../-pub-sub-result/channel.md) | [jvm]<br>abstract val [channel](../-pub-sub-result/channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [message](message.md) | [jvm]<br>abstract val [message](message.md): JsonElement<br>The actual message content |
| [publisher](../-pub-sub-result/publisher.md) | [jvm]<br>abstract val [publisher](../-pub-sub-result/publisher.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [subscription](../-pub-sub-result/subscription.md) | [jvm]<br>abstract val [subscription](../-pub-sub-result/subscription.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [timetoken](../-pub-sub-result/timetoken.md) | [jvm]<br>abstract val [timetoken](../-pub-sub-result/timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? |
| [userMetadata](../-pub-sub-result/user-metadata.md) | [jvm]<br>abstract val [userMetadata](../-pub-sub-result/user-metadata.md): JsonElement? |

## Inheritors

| Name |
|---|
| [PNMessageResult](../-p-n-message-result/index.md) |
| [PNSignalResult](../-p-n-signal-result/index.md) |
