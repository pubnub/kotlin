//[pubnub-core-api](../../../index.md)/[com.pubnub.api](../index.md)/[BasePubNub](index.md)/[channelGroup](channel-group.md)

# channelGroup

[jvm]\
abstract fun [channelGroup](channel-group.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [ChannelGroup](index.md)

Create a handle to a [ChannelGroup](index.md) that can be used to obtain a [Subscription](index.md).

The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server state. It is therefore permitted to use this method whenever a representation of a channel group is required.

The returned [ChannelGroup](index.md) holds a reference to this PubNub instance internally.

#### Return

a [ChannelGroup](index.md) instance representing the channel group with the given [name](channel-group.md)

#### Parameters

jvm

| | |
|---|---|
| name | the name of the channel group to return. See more in the [documentation](https://www.pubnub.com/docs/general/channels/subscribe#channel-groups) |
