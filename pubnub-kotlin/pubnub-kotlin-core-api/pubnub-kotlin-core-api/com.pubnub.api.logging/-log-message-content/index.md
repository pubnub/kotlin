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
| [Error](-error/index.md) | [jvm]<br>data class [Error](-error/index.md)(val type: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, val message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val stack: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;? = null) : [LogMessageContent](index.md)<br>Error message with type, message, and stack trace. |
| [NetworkRequest](-network-request/index.md) | [jvm]<br>data class [NetworkRequest](-network-request/index.md)(val origin: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val path: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val query: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;? = null, val method: [HttpMethod](../-http-method/index.md), val headers: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;? = null, val formData: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;? = null, val body: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, val timeout: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)? = null, val identifier: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, val canceled: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, val failed: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false) : [LogMessageContent](index.md)<br>Network request message. |
| [NetworkResponse](-network-response/index.md) | [jvm]<br>data class [NetworkResponse](-network-response/index.md)(val url: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val status: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), val headers: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;? = null, val body: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null) : [LogMessageContent](index.md)<br>Network response message. |
| [Object](-object/index.md) | [jvm]<br>data class [Object](-object/index.md)(val arguments: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt;? = null, val operation: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null) : [LogMessageContent](index.md)<br>Dictionary/object message for object type logs. |
| [Text](-text/index.md) | [jvm]<br>data class [Text](-text/index.md)(val message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) : [LogMessageContent](index.md)<br>Plain string message for text type logs. |
