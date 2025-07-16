//[pubnub-kotlin-docs](../../index.md)/[com.pubnub.docs.messageReactions](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [MessageReactionsOthers](-message-reactions-others/index.md) | [jvm]<br>class [MessageReactionsOthers](-message-reactions-others/index.md) |

## Functions

| Name | Summary |
|---|---|
| [addNewMessageAction](add-new-message-action.md) | [jvm]<br>fun [addNewMessageAction](add-new-message-action.md)(pubnub: [PubNub](../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api/-pub-nub/index.md), channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), messageTimetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html))<br>Helper function to add a new message action |
| [getMessageActionsWithPaging](get-message-actions-with-paging.md) | [jvm]<br>fun [getMessageActionsWithPaging](get-message-actions-with-paging.md)(pubNub: [PubNub](../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api/-pub-nub/index.md), channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), start: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)?, callback: (actions: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[PNMessageAction](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md)&gt;) -&gt; [Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html))<br>Fetches 5 message reactions at a time, recursively and in a paged manner. |
| [main](main.md) | [jvm]<br>fun [main](main.md)() |
