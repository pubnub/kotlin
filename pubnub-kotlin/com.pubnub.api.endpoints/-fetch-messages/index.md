//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints](../index.md)/[FetchMessages](index.md)

# FetchMessages

[jvm]\
class [FetchMessages](index.md) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[FetchMessagesEnvelope](../../com.pubnub.api.models.server/-fetch-messages-envelope/index.md), [PNFetchMessagesResult](../../com.pubnub.api.models.consumer.history/-p-n-fetch-messages-result/index.md)&gt;

## See also

jvm

| | |
|---|---|
| [com.pubnub.api.PubNub](../../com.pubnub.api/-pub-nub/fetch-messages.md) |  |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#893986951%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [async](index.md#893986951%2FFunctions%2F-1216412040)(callback: (result: [PNFetchMessagesResult](../../com.pubnub.api.models.consumer.history/-p-n-fetch-messages-result/index.md)?, status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>Executes the call asynchronously. This function does not block the thread. |
| [operationType](operation-type.md) | [jvm]<br>open override fun [operationType](operation-type.md)(): [PNOperationType.PNFetchMessagesOperation](../../com.pubnub.api.enums/-p-n-operation-type/-p-n-fetch-messages-operation/index.md) |
| [retry](../../com.pubnub.api/-endpoint/retry.md) | [jvm]<br>open override fun [retry](../../com.pubnub.api/-endpoint/retry.md)() |
| [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md) | [jvm]<br>open override fun [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md)()<br>Cancel the operation but do not alert anybody, useful for restarting the heartbeats and subscribe loops. |
| [sync](../../com.pubnub.api/-endpoint/sync.md) | [jvm]<br>open override fun [sync](../../com.pubnub.api/-endpoint/sync.md)(): [PNFetchMessagesResult](../../com.pubnub.api.models.consumer.history/-p-n-fetch-messages-result/index.md)?<br>Executes the call synchronously. This function blocks the thread. |

## Properties

| Name | Summary |
|---|---|
| [channels](channels.md) | [jvm]<br>val [channels](channels.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [includeMessageActions](include-message-actions.md) | [jvm]<br>val [includeMessageActions](include-message-actions.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeMessageType](include-message-type.md) | [jvm]<br>val [includeMessageType](include-message-type.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeMeta](include-meta.md) | [jvm]<br>val [includeMeta](include-meta.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeUUID](include-u-u-i-d.md) | [jvm]<br>val [includeUUID](include-u-u-i-d.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [page](page.md) | [jvm]<br>val [page](page.md): [PNBoundedPage](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md) |
| [queryParam](../../com.pubnub.api/-endpoint/query-param.md) | [jvm]<br>val [queryParam](../../com.pubnub.api/-endpoint/query-param.md): [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;<br>Key-value object to pass with every PubNub API operation. Used for debugging purposes. todo: it should be removed! |
