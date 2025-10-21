//[pubnub-kotlin-core-api](../../../../index.md)/[com.pubnub.api.logging](../../index.md)/[LogMessageContent](../index.md)/[NetworkResponse](index.md)

# NetworkResponse

[jvm]\
data class [NetworkResponse](index.md)(val url: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val status: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), val headers: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;? = null, val body: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null) : [LogMessageContent](../index.md)

Network response message.

## Constructors

| | |
|---|---|
| [NetworkResponse](-network-response.md) | [jvm]<br>constructor(url: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), status: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), headers: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;? = null, body: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null) |

## Properties

| Name | Summary |
|---|---|
| [body](body.md) | [jvm]<br>@SerializedName(value = &quot;body&quot;)<br>val [body](body.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null |
| [headers](headers.md) | [jvm]<br>@SerializedName(value = &quot;headers&quot;)<br>val [headers](headers.md): [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;? = null |
| [status](status.md) | [jvm]<br>@SerializedName(value = &quot;status&quot;)<br>val [status](status.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [url](url.md) | [jvm]<br>@SerializedName(value = &quot;url&quot;)<br>val [url](url.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
