//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.logging](../index.md)/[LogMessage](index.md)

# LogMessage

class [LogMessage](index.md)(val message: [LogMessageContent](../-log-message-content/index.md), val details: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, val type: [LogMessageType](../-log-message-type/index.md) = message.inferType(), val location: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, val pubNubId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, val logLevel: Level? = null, val timestamp: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = LocalTime.now().format(DateTimeFormatter.ofPattern(&quot;HH:mm:ss.SSS&quot;)))

Represents a structured log message with validation and sanitization.

#### Parameters

jvm

| | |
|---|---|
| pubNubId | The PubNub instance identifier |
| logLevel | The logging level |
| location | The source location of the log |
| type | The type of log message (automatically inferred from message content) |
| message | The log message content |
| details | Optional additional details (will be sanitized) |
| timestamp | The timestamp when the log was created |

## Constructors

| | |
|---|---|
| [LogMessage](-log-message.md) | [jvm]<br>constructor(message: [LogMessageContent](../-log-message-content/index.md), details: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, type: [LogMessageType](../-log-message-type/index.md) = message.inferType(), location: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, pubNubId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, logLevel: Level? = null, timestamp: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = LocalTime.now().format(DateTimeFormatter.ofPattern(&quot;HH:mm:ss.SSS&quot;))) |

## Properties

| Name | Summary |
|---|---|
| [details](details.md) | [jvm]<br>@SerializedName(value = &quot;details&quot;)<br>val [details](details.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null |
| [location](location.md) | [jvm]<br>@SerializedName(value = &quot;location&quot;)<br>val [location](location.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null |
| [logLevel](log-level.md) | [jvm]<br>@SerializedName(value = &quot;logLevel&quot;)<br>val [logLevel](log-level.md): Level? = null |
| [message](message.md) | [jvm]<br>@SerializedName(value = &quot;message&quot;)<br>val [message](message.md): [LogMessageContent](../-log-message-content/index.md) |
| [pubNubId](pub-nub-id.md) | [jvm]<br>@SerializedName(value = &quot;pubNubId&quot;)<br>val [pubNubId](pub-nub-id.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null |
| [timestamp](timestamp.md) | [jvm]<br>@SerializedName(value = &quot;timestamp&quot;)<br>val [timestamp](timestamp.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [type](type.md) | [jvm]<br>@SerializedName(value = &quot;type&quot;)<br>val [type](type.md): [LogMessageType](../-log-message-type/index.md) |
