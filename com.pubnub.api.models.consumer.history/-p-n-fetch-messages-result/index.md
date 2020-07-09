[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.history](../index.md) / [PNFetchMessagesResult](./index.md)

# PNFetchMessagesResult

`class PNFetchMessagesResult`

Result of the [PubNub.fetchMessages](../../com.pubnub.api/-pub-nub/fetch-messages.md) operation.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Result of the [PubNub.fetchMessages](../../com.pubnub.api/-pub-nub/fetch-messages.md) operation.`PNFetchMessagesResult(channels: `[`HashMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`PNFetchMessageItem`](../-p-n-fetch-message-item/index.md)`>>)` |

### Properties

| Name | Summary |
|---|---|
| [channels](channels.md) | Map of channels and their respective lists of [PNFetchMessageItem](../-p-n-fetch-message-item/index.md).`val channels: `[`HashMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`PNFetchMessageItem`](../-p-n-fetch-message-item/index.md)`>>` |
