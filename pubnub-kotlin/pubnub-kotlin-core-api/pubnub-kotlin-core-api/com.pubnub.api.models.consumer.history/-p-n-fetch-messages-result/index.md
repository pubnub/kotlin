//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.models.consumer.history](../index.md)/[PNFetchMessagesResult](index.md)

# PNFetchMessagesResult

[common]\
data class [PNFetchMessagesResult](index.md)(val channels: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[PNFetchMessageItem](../-p-n-fetch-message-item/index.md)&gt;&gt;, val page: [PNBoundedPage](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md)?)

Result of the FetchMessages operation.

## Constructors

| | |
|---|---|
| [PNFetchMessagesResult](-p-n-fetch-messages-result.md) | [common]<br>constructor(channels: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[PNFetchMessageItem](../-p-n-fetch-message-item/index.md)&gt;&gt;, page: [PNBoundedPage](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md)?) |

## Properties

| Name | Summary |
|---|---|
| [channels](channels.md) | [common]<br>val [channels](channels.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[PNFetchMessageItem](../-p-n-fetch-message-item/index.md)&gt;&gt;<br>Map of channels and their respective lists of [PNFetchMessageItem](../-p-n-fetch-message-item/index.md). |
| [page](page.md) | [common]<br>val [page](page.md): [PNBoundedPage](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md)? |
