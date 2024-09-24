//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java](../index.md)/[PubNub](index.md)/[history](history.md)

# history

[jvm]\
abstract fun [history](history.md)(): [History](../../com.pubnub.api.java.endpoints/-history/index.md)

Fetch historical messages of a channel.

It is possible to control how messages are returned and in what order, for example you can:

- 
   Search for messages starting on the newest end of the timeline (default behavior - `reverse = false`)
- 
   Search for messages from the oldest end of the timeline by setting `reverse` to `true`.
- 
   Page through results by providing a `start` OR `end` timetoken.
- 
   Retrieve a slice of the time line by providing both a `start` AND `end` timetoken.
- 
   Limit the number of messages to a specific quantity using the `count` parameter.

**Start** & **End parameter usage clarity:**

- 
   If only the `start` parameter is specified (without `end`), you will receive messages that are older than and up to that `start` timetoken value.
- 
   If only the `end` parameter is specified (without `start`) you will receive messages that match that end timetoken value and newer.
- 
   Specifying values for both start and end parameters will return messages between those timetoken values (inclusive on the `end` value)
- 
   Keep in mind that you will still receive a maximum of 100 messages even if there are more messages that meet the timetoken values. Iterative calls to history adjusting the start timetoken is necessary to page through the full set of results if more than 100 messages meet the timetoken values.
