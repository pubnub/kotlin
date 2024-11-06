//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[channelGroup](channel-group.md)

# channelGroup

[common]\
expect abstract fun [channelGroup](channel-group.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [ChannelGroup](../../com.pubnub.api.v2.entities/-channel-group/index.md)actual abstract fun [channelGroup](channel-group.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [ChannelGroup](../../com.pubnub.api.v2.entities/-channel-group/index.md)

[jvm]\
actual abstract fun [channelGroup](channel-group.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [ChannelGroup](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.entities/-channel-group/index.md)

Create a handle to a [ChannelGroup](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.entities/-channel-group/index.md) that can be used to obtain a [Subscription](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription/index.md).

The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server state. It is therefore permitted to use this method whenever a representation of a channel group is required.

The returned [ChannelGroup](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.entities/-channel-group/index.md) holds a reference to this [PubNub](index.md) instance internally.

#### Return

a [ChannelGroup](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.entities/-channel-group/index.md) instance representing the channel group with the given [name](channel-group.md)

#### Parameters

jvm

| | |
|---|---|
| name | the name of the channel group to return. See more in the [documentation](https://www.pubnub.com/docs/general/channels/subscribe#channel-groups) |
