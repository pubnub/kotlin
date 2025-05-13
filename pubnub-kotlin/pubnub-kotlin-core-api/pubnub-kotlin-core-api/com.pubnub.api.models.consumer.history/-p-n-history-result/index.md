//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.models.consumer.history](../index.md)/[PNHistoryResult](index.md)

# PNHistoryResult

[common]\
class [PNHistoryResult](index.md)(val messages: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[PNHistoryItemResult](../-p-n-history-item-result/index.md)&gt;, val startTimetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html), val endTimetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html))

Result of a History operation.

## Constructors

| | |
|---|---|
| [PNHistoryResult](-p-n-history-result.md) | [common]<br>constructor(messages: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[PNHistoryItemResult](../-p-n-history-item-result/index.md)&gt;, startTimetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html), endTimetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [endTimetoken](end-timetoken.md) | [common]<br>val [endTimetoken](end-timetoken.md): [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)<br>End timetoken of the returned list of messages. |
| [messages](messages.md) | [common]<br>val [messages](messages.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[PNHistoryItemResult](../-p-n-history-item-result/index.md)&gt;<br>List of messages as instances of [PNHistoryItemResult](../-p-n-history-item-result/index.md). |
| [startTimetoken](start-timetoken.md) | [common]<br>val [startTimetoken](start-timetoken.md): [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)<br>Start timetoken of the returned list of messages. |
