//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[unsubscribe](unsubscribe.md)

# unsubscribe

[jvm]\

@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)

fun [unsubscribe](unsubscribe.md)(channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptyList(), channelGroups: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptyList())

When subscribed to a single channel, this function causes the client to issue a leave from the channel and close any open socket to the PubNub Network.

For multiplexed channels, the specified channel(s) will be removed and the socket remains open until there are no more channels remaining in the list.

- 
   **WARNING** Unsubscribing from all the channel(s) and then subscribing to a new channel Y isn't the same as Subscribing to channel Y and then unsubscribing from the previously subscribed channel(s).

Unsubscribing from all the channels resets the timetoken and thus, there could be some gaps in the subscription that may lead to a message loss.

## Parameters

jvm

| | |
|---|---|
| channels | Channels to subscribe/unsubscribe. Either `channel` or [channelGroups](unsubscribe.md) are required. |
| channelGroups | Channel groups to subscribe/unsubscribe. Either `channelGroups` or [channels](unsubscribe.md) are required. |
