//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[channel](channel.md)

# channel

[common, native]\
[common]\
expect abstract fun [channel](channel.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Channel](../../com.pubnub.api.v2.entities/-channel/index.md)

[native]\
actual abstract fun [channel](channel.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Channel](../../com.pubnub.api.v2.entities/-channel/index.md)

[jvm]\
actual abstract fun [channel](channel.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Channel](../../com.pubnub.api.v2.entities/-channel/index.md)

Create a handle to a [Channel](../../com.pubnub.api.v2.entities/-channel/index.md) that can be used to obtain a [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md).

The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server state. It is therefore permitted to use this method whenever a representation of a channel is required.

The returned [Channel](../../com.pubnub.api.v2.entities/-channel/index.md) holds a reference to this [PubNub](index.md) instance internally.

#### Return

a [Channel](../../com.pubnub.api.v2.entities/-channel/index.md) instance representing the channel with the given [name](channel.md)

#### Parameters

jvm

| | |
|---|---|
| name | the name of the channel to return. Supports wildcards by ending it with &quot;.*&quot;. See more in the [documentation](https://www.pubnub.com/docs/general/channels/overview) |
