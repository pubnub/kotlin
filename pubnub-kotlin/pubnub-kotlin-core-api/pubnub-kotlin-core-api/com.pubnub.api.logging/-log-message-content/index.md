//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.logging](../index.md)/[LogMessageContent](index.md)

# LogMessageContent

sealed class [LogMessageContent](index.md)

Sealed class representing different types of log message content. This provides type safety when working with different message types.

#### Inheritors

| |
|---|
| [Text](-text/index.md) |
| [Object](-object/index.md) |
| [Error](-error/index.md) |
| [NetworkRequest](-network-request/index.md) |
| [NetworkResponse](-network-response/index.md) |

## Types

| Name | Summary |
|---|---|
| [Error](-error/index.md) | [jvm]<br>data class [Error](-error/index.md)(val message: [ErrorDetails](../-error-details/index.md)) : [LogMessageContent](index.md)<br>Error message with type, message, and stack trace. |
| [NetworkRequest](-network-request/index.md) | [jvm]<br>data class [NetworkRequest](-network-request/index.md)(val message: [NetworkLog.Request](../-network-log/-request/index.md)) : [LogMessageContent](index.md)<br>Network request message. |
| [NetworkResponse](-network-response/index.md) | [jvm]<br>data class [NetworkResponse](-network-response/index.md)(val message: [NetworkLog.Response](../-network-log/-response/index.md)) : [LogMessageContent](index.md)<br>Network response message. |
| [Object](-object/index.md) | [jvm]<br>data class [Object](-object/index.md)(val message: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt;) : [LogMessageContent](index.md)<br>Dictionary/object message for object type logs. |
| [Text](-text/index.md) | [jvm]<br>data class [Text](-text/index.md)(val message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) : [LogMessageContent](index.md)<br>Plain string message for text type logs. |
