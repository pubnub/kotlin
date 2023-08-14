//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.history](../index.md)/[PNHistoryResult](index.md)

# PNHistoryResult

[jvm]\
class [PNHistoryResult](index.md)

Result of the [PubNub.history](../../com.pubnub.api/-pub-nub/history.md) operation.

## Properties

| Name | Summary |
|---|---|
| [endTimetoken](end-timetoken.md) | [jvm]<br>val [endTimetoken](end-timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>End timetoken of the returned list of messages. |
| [messages](messages.md) | [jvm]<br>val [messages](messages.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNHistoryItemResult](../-p-n-history-item-result/index.md)&gt;<br>List of messages as instances of [PNHistoryItemResult](../-p-n-history-item-result/index.md). |
| [startTimetoken](start-timetoken.md) | [jvm]<br>val [startTimetoken](start-timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>Start timetoken of the returned list of messages. |
