//[pubnub-gson](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[messageCounts](message-counts.md)

# messageCounts

[jvm]\
abstract fun [messageCounts](message-counts.md)(): [MessageCounts](../../com.pubnub.api.endpoints/-message-counts/index.md)

Fetches the number of messages published on one or more channels since a given time. The count returned is the number of messages in history with a timetoken value greater than the passed value in the [MessageCounts.channelsTimetoken](../../../../pubnub-gson/com.pubnub.api.endpoints/-message-counts/channels-timetoken.md) parameter.
