//[pubnub-kotlin](../../index.md)/[com.pubnub.api.models.server.history](index.md)

# Package com.pubnub.api.models.server.history

## Types

| Name | Summary |
|---|---|
| [ServerFetchMessageItem](-server-fetch-message-item/index.md) | [jvm]<br>data class [ServerFetchMessageItem](-server-fetch-message-item/index.md)(val uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, val message: JsonElement, val meta: JsonElement?, val timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), val actions: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Action](../com.pubnub.api.models.consumer.history/-action/index.md)&gt;&gt;&gt;? = null, val messageType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?) |
| [ServerFetchMessagesResult](-server-fetch-messages-result/index.md) | [jvm]<br>data class [ServerFetchMessagesResult](-server-fetch-messages-result/index.md)(val channels: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[ServerFetchMessageItem](-server-fetch-message-item/index.md)&gt;&gt;, val page: [PNBoundedPage](../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md)?) |
