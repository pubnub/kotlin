//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[getMessageActions](get-message-actions.md)

# getMessageActions

[common, native]\
[common]\
expect abstract fun [getMessageActions](get-message-actions.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), page: [PNBoundedPage](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md) = PNBoundedPage()): [GetMessageActions](../../com.pubnub.api.endpoints.message_actions/-get-message-actions/index.md)

[native]\
actual abstract fun [getMessageActions](get-message-actions.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), page: [PNBoundedPage](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md)): [GetMessageActions](../../com.pubnub.api.endpoints.message_actions/-get-message-actions/index.md)

[jvm]\
actual abstract fun [getMessageActions](get-message-actions.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), page: [PNBoundedPage](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md)): [GetMessageActions](../../com.pubnub.api.endpoints.message_actions/-get-message-actions/index.md)

Get a list of message actions in a channel. Returns a list of actions in the response.

#### Parameters

jvm

| | |
|---|---|
| channel | Channel to fetch message actions from. |
| page | The paging object used for pagination. @see [PNBoundedPage](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md) |

[jvm]\
abstract fun [~~getMessageActions~~](get-message-actions.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), start: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, end: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null): [GetMessageActions](../../com.pubnub.api.endpoints.message_actions/-get-message-actions/index.md)

---

### Deprecated

Use getMessageActions(String, PNBoundedPage) instead

#### Replace with

```kotlin
import com.pubnub.api.models.consumer.PNBoundedPage

```
```kotlin
getMessageActions(channel = channel, page = PNBoundedPage(start = start, end = end, limit = limit))
```
---

Get a list of message actions in a channel. Returns a list of actions in the response.

#### Parameters

jvm

| | |
|---|---|
| channel | Channel to fetch message actions from. |
| start | Message Action timetoken denoting the start of the range requested     (return values will be less than start). |
| end | Message Action timetoken denoting the end of the range requested     (return values will be greater than or equal to end). |
| limit | Specifies the number of message actions to return in response. |
