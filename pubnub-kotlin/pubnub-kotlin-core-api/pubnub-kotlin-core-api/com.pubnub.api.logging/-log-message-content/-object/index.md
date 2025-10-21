//[pubnub-kotlin-core-api](../../../../index.md)/[com.pubnub.api.logging](../../index.md)/[LogMessageContent](../index.md)/[Object](index.md)

# Object

[jvm]\
data class [Object](index.md)(val arguments: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt;? = null, val operation: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null) : [LogMessageContent](../index.md)

Dictionary/object message for object type logs.

## Constructors

| | |
|---|---|
| [Object](-object.md) | [jvm]<br>constructor(arguments: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt;? = null, operation: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null) |

## Properties

| Name | Summary |
|---|---|
| [arguments](arguments.md) | [jvm]<br>@SerializedName(value = &quot;arguments&quot;)<br>val [arguments](arguments.md): [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt;? = null |
| [operation](operation.md) | [jvm]<br>@SerializedName(value = &quot;operation&quot;)<br>val [operation](operation.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null |
