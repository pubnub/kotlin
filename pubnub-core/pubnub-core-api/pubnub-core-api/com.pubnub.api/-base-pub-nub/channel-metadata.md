//[pubnub-core-api](../../../index.md)/[com.pubnub.api](../index.md)/[BasePubNub](index.md)/[channelMetadata](channel-metadata.md)

# channelMetadata

[jvm]\
abstract fun [channelMetadata](channel-metadata.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [ChannelMetadata](index.md)

Create a handle to a [ChannelMetadata](index.md) object that can be used to obtain a [Subscription](index.md) to metadata events.

The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server state. It is therefore permitted to use this method whenever a representation of a metadata channel is required.

The returned [ChannelMetadata](index.md) holds a reference to this PubNub instance internally.

#### Return

a [ChannelMetadata](index.md) instance representing the channel metadata with the given [id](channel-metadata.md)

#### Parameters

jvm

| | |
|---|---|
| id | the id of the channel metadata to return. See more in the [documentation](https://www.pubnub.com/docs/general/metadata/channel-metadata) |
