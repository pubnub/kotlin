//[pubnub-core-api](../../../index.md)/[com.pubnub.api](../index.md)/[BasePubNub](index.md)/[channel](channel.md)

# channel

[jvm]\
abstract fun [channel](channel.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Channel](index.md)

Create a handle to a [Channel](index.md) that can be used to obtain a [Subscription](index.md).

The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server state. It is therefore permitted to use this method whenever a representation of a channel is required.

The returned [Channel](index.md) holds a reference to this PubNub instance internally.

#### Return

a [Channel](index.md) instance representing the channel with the given [name](channel.md)

#### Parameters

jvm

| | |
|---|---|
| name | the name of the channel to return. Supports wildcards by ending it with &quot;.*&quot;. See more in the [documentation](https://www.pubnub.com/docs/general/channels/overview) |
