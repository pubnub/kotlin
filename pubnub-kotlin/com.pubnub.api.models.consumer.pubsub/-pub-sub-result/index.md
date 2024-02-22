//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.pubsub](../index.md)/[PubSubResult](index.md)

# PubSubResult

[jvm]\
interface [PubSubResult](index.md) : [PNEvent](../-p-n-event/index.md)

## Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | [jvm]<br>abstract override val [channel](channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [publisher](publisher.md) | [jvm]<br>abstract val [publisher](publisher.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [subscription](subscription.md) | [jvm]<br>abstract override val [subscription](subscription.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [timetoken](timetoken.md) | [jvm]<br>abstract override val [timetoken](timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? |
| [userMetadata](user-metadata.md) | [jvm]<br>abstract val [userMetadata](user-metadata.md): JsonElement? |

## Inheritors

| Name |
|---|
| [BasePubSubResult](../-base-pub-sub-result/index.md) |
| [MessageResult](../-message-result/index.md) |
| [PNMessageResult](../-p-n-message-result/index.md) |
| [PNSignalResult](../-p-n-signal-result/index.md) |
| [PNMessageActionResult](../../com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md) |
| [PNObjectEventResult](../../com.pubnub.api.models.consumer.pubsub.objects/-p-n-object-event-result/index.md) |
