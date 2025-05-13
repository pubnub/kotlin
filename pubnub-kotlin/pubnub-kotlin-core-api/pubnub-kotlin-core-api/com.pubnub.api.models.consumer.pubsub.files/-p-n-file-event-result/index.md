//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.models.consumer.pubsub.files](../index.md)/[PNFileEventResult](index.md)

# PNFileEventResult

[common]\
class [PNFileEventResult](index.md)(val channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val timetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)?, val publisher: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, val message: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?, val file: [PNDownloadableFile](../../com.pubnub.api.models.consumer.files/-p-n-downloadable-file/index.md), val jsonMessage: [JsonElement](../../com.pubnub.api/-json-element/index.md), val subscription: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, val error: [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null, val customMessageType: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null) : [PNEvent](../../com.pubnub.api.models.consumer.pubsub/-p-n-event/index.md)

## Constructors

| | |
|---|---|
| [PNFileEventResult](-p-n-file-event-result.md) | [common]<br>constructor(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), timetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)?, publisher: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, message: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?, file: [PNDownloadableFile](../../com.pubnub.api.models.consumer.files/-p-n-downloadable-file/index.md), jsonMessage: [JsonElement](../../com.pubnub.api/-json-element/index.md), subscription: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, error: [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null, customMessageType: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null) |

## Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | [common]<br>open override val [channel](channel.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [customMessageType](custom-message-type.md) | [common]<br>val [customMessageType](custom-message-type.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null |
| [error](error.md) | [common]<br>val [error](error.md): [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null |
| [file](file.md) | [common]<br>val [file](file.md): [PNDownloadableFile](../../com.pubnub.api.models.consumer.files/-p-n-downloadable-file/index.md) |
| [jsonMessage](json-message.md) | [common]<br>val [jsonMessage](json-message.md): [JsonElement](../../com.pubnub.api/-json-element/index.md) |
| [message](message.md) | [common]<br>val [message](message.md): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)? |
| [publisher](publisher.md) | [common]<br>val [publisher](publisher.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [subscription](subscription.md) | [common]<br>open override val [subscription](subscription.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null |
| [timetoken](timetoken.md) | [common]<br>open override val [timetoken](timetoken.md): [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)? |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [common]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [common]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
