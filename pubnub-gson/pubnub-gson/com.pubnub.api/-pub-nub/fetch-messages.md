//[pubnub-gson](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[fetchMessages](fetch-messages.md)

# fetchMessages

[jvm]\
abstract fun [fetchMessages](fetch-messages.md)(): [FetchMessages](../../com.pubnub.api.endpoints/-fetch-messages/index.md)

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
