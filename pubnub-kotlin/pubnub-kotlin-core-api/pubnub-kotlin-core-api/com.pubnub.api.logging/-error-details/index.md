//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.logging](../index.md)/[ErrorDetails](index.md)

# ErrorDetails

[jvm]\
data class [ErrorDetails](index.md)(val type: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, val message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val stack: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;? = null)

## Constructors

| | |
|---|---|
| [ErrorDetails](-error-details.md) | [jvm]<br>constructor(type: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), stack: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;? = null) |

## Properties

| Name | Summary |
|---|---|
| [message](message.md) | [jvm]<br>@SerializedName(value = &quot;message&quot;)<br>val [message](message.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [stack](stack.md) | [jvm]<br>@SerializedName(value = &quot;stack&quot;)<br>val [stack](stack.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;? = null |
| [type](type.md) | [jvm]<br>@SerializedName(value = &quot;type&quot;)<br>val [type](type.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null |
