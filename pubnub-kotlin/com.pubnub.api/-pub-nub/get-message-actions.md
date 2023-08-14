//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[getMessageActions](get-message-actions.md)

# getMessageActions

[jvm]\
~~fun~~ [~~getMessageActions~~](get-message-actions.md)~~(~~channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), start: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, end: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null~~)~~~~:~~ [GetMessageActions](../../com.pubnub.api.endpoints.message_actions/-get-message-actions/index.md)

Get a list of message actions in a channel. Returns a list of actions in the response.

## Parameters

jvm

| | |
|---|---|
| channel | Channel to fetch message actions from. |
| start | Message Action timetoken denoting the start of the range requested     (return values will be less than start). |
| end | Message Action timetoken denoting the end of the range requested     (return values will be greater than or equal to end). |
| limit | Specifies the number of message actions to return in response. |

[jvm]\
fun [getMessageActions](get-message-actions.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), page: [PNBoundedPage](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md) = PNBoundedPage()): [GetMessageActions](../../com.pubnub.api.endpoints.message_actions/-get-message-actions/index.md)

Get a list of message actions in a channel. Returns a list of actions in the response.

## Parameters

jvm

| | |
|---|---|
| channel | Channel to fetch message actions from. |
| page | The paging object used for pagination. @see [PNBoundedPage](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md) |
