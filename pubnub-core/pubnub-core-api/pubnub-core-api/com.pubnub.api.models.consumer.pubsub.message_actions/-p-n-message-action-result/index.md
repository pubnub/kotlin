//[pubnub-core-api](../../../index.md)/[com.pubnub.api.models.consumer.pubsub.message_actions](../index.md)/[PNMessageActionResult](index.md)

# PNMessageActionResult

[jvm]\
class [PNMessageActionResult](index.md)(result: [BasePubSubResult](../../com.pubnub.api.models.consumer.pubsub/-base-pub-sub-result/index.md), val event: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val data: [PNMessageAction](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md)) : [ObjectResult](../../com.pubnub.api.models.consumer.pubsub.objects/-object-result/index.md)&lt;[PNMessageAction](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md)&gt; , [PubSubResult](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/index.md)

Wrapper around message actions received in SubscribeCallback.messageAction.

## Constructors

| | |
|---|---|
| [PNMessageActionResult](-p-n-message-action-result.md) | [jvm]<br>constructor(result: [BasePubSubResult](../../com.pubnub.api.models.consumer.pubsub/-base-pub-sub-result/index.md), event: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), data: [PNMessageAction](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [channel](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/channel.md) | [jvm]<br>open override val [channel](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [data](data.md) | [jvm]<br>open override val [data](data.md): [PNMessageAction](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md)<br>The actual message action. |
| [event](event.md) | [jvm]<br>open override val [event](event.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The message action event. Could be `added` or `removed`. |
| [messageAction](message-action.md) | [jvm]<br>val [messageAction](message-action.md): [PNMessageAction](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md) |
| [publisher](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/publisher.md) | [jvm]<br>open override val [publisher](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/publisher.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [subscription](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/subscription.md) | [jvm]<br>open override val [subscription](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/subscription.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [timetoken](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/timetoken.md) | [jvm]<br>open override val [timetoken](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? |
| [userMetadata](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/user-metadata.md) | [jvm]<br>open override val [userMetadata](../../com.pubnub.api.models.consumer.pubsub/-pub-sub-result/user-metadata.md): JsonElement? |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [jvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [jvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
