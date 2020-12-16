[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.message_actions](../index.md) / [PNGetMessageActionsResult](./index.md)

# PNGetMessageActionsResult

`class PNGetMessageActionsResult`

Result for the [PubNub.getMessageActions](../../com.pubnub.api/-pub-nub/get-message-actions.md) API operation.

### Properties

| Name | Summary |
|---|---|
| [actions](actions.md) | List of message actions for a certain message in a certain channel.`val actions: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`PNMessageAction`](../-p-n-message-action/index.md)`>` |
| [page](page.md) | Information about next page. When null there's no next page.`val page: `[`PNBoundedPage`](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md)`?` |
