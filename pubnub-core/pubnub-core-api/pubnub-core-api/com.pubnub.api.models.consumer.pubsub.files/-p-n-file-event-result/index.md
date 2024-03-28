//[pubnub-core-api](../../../index.md)/[com.pubnub.api.models.consumer.pubsub.files](../index.md)/[PNFileEventResult](index.md)

# PNFileEventResult

[jvm]\
class [PNFileEventResult](index.md)(val channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)?, val publisher: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, val message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, val file: [PNDownloadableFile](../../com.pubnub.api.models.consumer.files/-p-n-downloadable-file/index.md), val jsonMessage: JsonElement, val subscription: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val error: [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null) : [PNEvent](../../com.pubnub.api.models.consumer.pubsub/-p-n-event/index.md)

## Constructors

| | |
|---|---|
| [PNFileEventResult](-p-n-file-event-result.md) | [jvm]<br>constructor(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)?, publisher: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, file: [PNDownloadableFile](../../com.pubnub.api.models.consumer.files/-p-n-downloadable-file/index.md), jsonMessage: JsonElement, subscription: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, error: [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null) |

## Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | [jvm]<br>open override val [channel](channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [error](error.md) | [jvm]<br>val [error](error.md): [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null |
| [file](file.md) | [jvm]<br>val [file](file.md): [PNDownloadableFile](../../com.pubnub.api.models.consumer.files/-p-n-downloadable-file/index.md) |
| [jsonMessage](json-message.md) | [jvm]<br>val [jsonMessage](json-message.md): JsonElement |
| [message](message.md) | [jvm]<br>val [message](message.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? |
| [publisher](publisher.md) | [jvm]<br>val [publisher](publisher.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [subscription](subscription.md) | [jvm]<br>open override val [subscription](subscription.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null |
| [timetoken](timetoken.md) | [jvm]<br>open override val [timetoken](timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [jvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [jvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
