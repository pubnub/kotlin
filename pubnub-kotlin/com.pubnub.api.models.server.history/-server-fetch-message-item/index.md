//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.server.history](../index.md)/[ServerFetchMessageItem](index.md)

# ServerFetchMessageItem

[jvm]\
data class [ServerFetchMessageItem](index.md)(val uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, val message: JsonElement, val meta: JsonElement?, val timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), val actions: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Action](../../com.pubnub.api.models.consumer.history/-action/index.md)&gt;&gt;&gt;? = null, val messageType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?)

## Constructors

| | |
|---|---|
| [ServerFetchMessageItem](-server-fetch-message-item.md) | [jvm]<br>fun [ServerFetchMessageItem](-server-fetch-message-item.md)(uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, message: JsonElement, meta: JsonElement?, timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), actions: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Action](../../com.pubnub.api.models.consumer.history/-action/index.md)&gt;&gt;&gt;? = null, messageType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?) |

## Properties

| Name | Summary |
|---|---|
| [actions](actions.md) | [jvm]<br>val [actions](actions.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Action](../../com.pubnub.api.models.consumer.history/-action/index.md)&gt;&gt;&gt;? = null |
| [message](message.md) | [jvm]<br>val [message](message.md): JsonElement |
| [messageType](message-type.md) | [jvm]<br>@SerializedName(value = &quot;message_type&quot;)<br>val [messageType](message-type.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? |
| [meta](meta.md) | [jvm]<br>val [meta](meta.md): JsonElement? |
| [timetoken](timetoken.md) | [jvm]<br>val [timetoken](timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [uuid](uuid.md) | [jvm]<br>val [uuid](uuid.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
