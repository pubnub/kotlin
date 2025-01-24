//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[channelMetadata](channel-metadata.md)

# channelMetadata

[common]\
expect abstract fun [channelMetadata](channel-metadata.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [ChannelMetadata](../../com.pubnub.api.v2.entities/-channel-metadata/index.md)actual abstract fun [channelMetadata](channel-metadata.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [ChannelMetadata](../../com.pubnub.api.v2.entities/-channel-metadata/index.md)

[jvm]\
actual abstract fun [channelMetadata](channel-metadata.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [ChannelMetadata](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.entities/-channel-metadata/index.md)

Create a handle to a [ChannelMetadata](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.entities/-channel-metadata/index.md) object that can be used to obtain a [Subscription](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription/index.md) to metadata events.

The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server state. It is therefore permitted to use this method whenever a representation of a metadata channel is required.

The returned [ChannelMetadata](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.entities/-channel-metadata/index.md) holds a reference to this [PubNub](index.md) instance internally.

#### Return

a [ChannelMetadata](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.entities/-channel-metadata/index.md) instance representing the channel metadata with the given [id](channel-metadata.md)

#### Parameters

jvm

| | |
|---|---|
| id | the id of the channel metadata to return. See more in the [documentation](https://www.pubnub.com/docs/general/metadata/channel-metadata) |
