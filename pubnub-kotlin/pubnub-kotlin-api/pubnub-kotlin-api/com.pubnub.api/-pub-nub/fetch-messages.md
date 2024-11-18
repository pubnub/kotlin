//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[fetchMessages](fetch-messages.md)

# fetchMessages

[common]\
expect abstract fun [fetchMessages](fetch-messages.md)(channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, page: [PNBoundedPage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer/-p-n-bounded-page/index.md) = PNBoundedPage(), includeUUID: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true, includeMeta: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, includeMessageActions: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, includeMessageType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true, includeCustomMessageType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false): [FetchMessages](../../com.pubnub.api.endpoints/-fetch-messages/index.md)actual abstract fun [fetchMessages](fetch-messages.md)(channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, page: [PNBoundedPage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer/-p-n-bounded-page/index.md), includeUUID: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), includeMeta: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), includeMessageActions: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), includeMessageType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), includeCustomMessageType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [FetchMessages](../../com.pubnub.api.endpoints/-fetch-messages/index.md)

[jvm]\
actual abstract fun [fetchMessages](fetch-messages.md)(channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, page: [PNBoundedPage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer/-p-n-bounded-page/index.md), includeUUID: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), includeMeta: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), includeMessageActions: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), includeMessageType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), includeCustomMessageType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [FetchMessages](../../com.pubnub.api.endpoints/-fetch-messages/index.md)

Fetch historical messages from multiple channels. The `includeMessageActions` flag also allows you to fetch message actions along with the messages.

It's possible to control how messages are returned and in what order. For example, you can:

- 
   Search for messages starting on the newest end of the timeline.
- 
   Search for messages from the oldest end of the timeline.
- 
   Page through results by providing a `start` OR `end` time token.
- 
   Retrieve a slice of the time line by providing both a `start` AND `end` time token.
- 
   Limit the number of messages to a specific quantity using the `limit` parameter.
- 
   Batch history returns up to 25 messages per channel, on a maximum of 500 channels. Use the start and end timestamps to page through the next batch of messages.

**Start** & **End parameter usage clarity:**

- 
   If you specify only the `start` parameter (without `end`), you will receive messages that are older than and up to that `start` timetoken.
- 
   If you specify only the `end` parameter (without `start`), you will receive messages from that `end` timetoken and newer.
- 
   Specify values for both `start` and `end` parameters to retrieve messages between those timetokens (inclusive of the `end` value).
- 
   Keep in mind that you will still receive a maximum of 25 messages even if there are more messages that meet the timetoken values.
- 
   Iterative calls to history adjusting the start timetoken is necessary to page through the full set of results if more than 25 messages meet the timetoken values.

#### Parameters

jvm

| | |
|---|---|
| channels | Channels to return history messages from. |
| page | The paging object used for pagination. @see [PNBoundedPage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer/-p-n-bounded-page/index.md) |
| includeUUID | Whether to include publisher uuid with each history message. Defaults to `true`. |
| includeMeta | Whether to include message metadata in response.     Defaults to `false`. |
| includeMessageActions | Whether to include message actions in response.     Defaults to `false`. |
| includeMessageType | Whether to include message type in response.     Defaults to `false`. |
| includeCustomMessageType | Whether to include custom message type in response. Default to `false` |

[jvm]\
abstract fun [~~fetchMessages~~](fetch-messages.md)(channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, maximumPerChannel: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, start: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, end: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, includeMeta: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, includeMessageActions: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, includeMessageType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true, includeCustomMessageType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false): [FetchMessages](../../com.pubnub.api.endpoints/-fetch-messages/index.md)

---

### Deprecated

Use fetchMessages(List&lt;String&gt;, PNBoundedPage, Boolean, Boolean, Boolean, Boolean, Boolean) instead

#### Replace with

```kotlin
import com.pubnub.api.models.consumer.PNBoundedPage

```
```kotlin
fetchMessages(channels = channels, page = PNBoundedPage(start = start, end = end, limit = maximumPerChannel),includeMeta = includeMeta, includeMessageActions = includeMessageActions, includeMessageType = includeMessageType)
```
---

Fetch historical messages from multiple channels. The `includeMessageActions` flag also allows you to fetch message actions along with the messages.

It's possible to control how messages are returned and in what order. For example, you can:

- 
   Search for messages starting on the newest end of the timeline.
- 
   Search for messages from the oldest end of the timeline.
- 
   Page through results by providing a `start` OR `end` time token.
- 
   Retrieve a slice of the time line by providing both a `start` AND `end` time token.
- 
   Limit the number of messages to a specific quantity using the `count` parameter.
- 
   Batch history returns up to 25 messages per channel, on a maximum of 500 channels. Use the start and end timestamps to page through the next batch of messages.

**Start** & **End parameter usage clarity:**

- 
   If you specify only the `start` parameter (without `end`), you will receive messages that are older than and up to that `start` timetoken.
- 
   If you specify only the `end` parameter (without `start`), you will receive messages from that `end` timetoken and newer.
- 
   Specify values for both `start` and `end` parameters to retrieve messages between those timetokens (inclusive of the `end` value).
- 
   Keep in mind that you will still receive a maximum of 25 messages even if there are more messages that meet the timetoken values.
- 
   Iterative calls to history adjusting the start timetoken is necessary to page through the full set of results if more than 25 messages meet the timetoken values.

#### Parameters

jvm

| | |
|---|---|
| channels | Channels to return history messages from. |
| maximumPerChannel | Specifies the number of historical messages to return per channel.     If [includeMessageActions](fetch-messages.md) is `false`, then `1` is the default (and maximum) value.     Otherwise it's `25`. |
| start | Timetoken delimiting the start of time slice (exclusive) to pull messages from. |
| end | Time token delimiting the end of time slice (inclusive) to pull messages from. |
| includeMeta | Whether to include message metadata in response.     Defaults to `false`. |
| includeMessageActions | Whether to include message actions in response.     Defaults to `false`. |
