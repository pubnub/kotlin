//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.pubsub.objects](../index.md)/[PNObjectEventResult](index.md)

# PNObjectEventResult

[jvm]\
data class [PNObjectEventResult](index.md)(result: [BasePubSubResult](../../com.pubnub.api.models.consumer.pubsub/-base-pub-sub-result/index.md), val extractedMessage: [PNObjectEventMessage](../-p-n-object-event-message/index.md)) : [PubSubResult](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/index.md)

## Constructors

| | |
|---|---|
| [PNObjectEventResult](-p-n-object-event-result.md) | [jvm]<br>fun [PNObjectEventResult](-p-n-object-event-result.md)(result: [BasePubSubResult](../../com.pubnub.api.models.consumer.pubsub/-base-pub-sub-result/index.md), extractedMessage: [PNObjectEventMessage](../-p-n-object-event-message/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [channel](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/channel.md) | [jvm]<br>open override val [channel](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [extractedMessage](extracted-message.md) | [jvm]<br>val [extractedMessage](extracted-message.md): [PNObjectEventMessage](../-p-n-object-event-message/index.md) |
| [publisher](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/publisher.md) | [jvm]<br>open override val [publisher](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/publisher.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [subscription](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/subscription.md) | [jvm]<br>open override val [subscription](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/subscription.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [timetoken](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/timetoken.md) | [jvm]<br>open override val [timetoken](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? |
| [userMetadata](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/user-metadata.md) | [jvm]<br>open override val [userMetadata](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/user-metadata.md): JsonElement? |
