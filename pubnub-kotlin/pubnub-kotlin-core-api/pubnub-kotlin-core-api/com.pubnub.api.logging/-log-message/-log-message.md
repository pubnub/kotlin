//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.logging](../index.md)/[LogMessage](index.md)/[LogMessage](-log-message.md)

# LogMessage

[jvm]\
constructor(message: [LogMessageContent](../-log-message-content/index.md), details: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, type: [LogMessageType](../-log-message-type/index.md) = message.inferType(), location: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, pubNubId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, logLevel: Level? = null, timestamp: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = LocalTime.now().format(DateTimeFormatter.ofPattern(&quot;HH:mm:ss.SSS&quot;)))

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
