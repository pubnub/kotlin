//[pubnub-kotlin-docs](../../index.md)/[com.pubnub.docs.messageReactions](index.md)/[getMessageActionsWithPaging](get-message-actions-with-paging.md)

# getMessageActionsWithPaging

[jvm]\
fun [getMessageActionsWithPaging](get-message-actions-with-paging.md)(pubNub: [PubNub](../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api/-pub-nub/index.md), channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), start: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)?, callback: (actions: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[PNMessageAction](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md)&gt;) -&gt; [Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html))

Fetches 5 message reactions at a time, recursively and in a paged manner.

#### Parameters

jvm

| | |
|---|---|
| channel | The channel where the message is published, to fetch message reactions from. |
| start | The timetoken which indicates from where to start fetching message reactions. |
| callback | The callback to dispatch fetched message reactions to. |
