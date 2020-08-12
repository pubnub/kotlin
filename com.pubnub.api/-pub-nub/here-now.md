[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [hereNow](./here-now.md)

# hereNow

`fun hereNow(channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList(), channelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList(), includeState: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, includeUUIDs: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`HereNow`](../../com.pubnub.api.endpoints.presence/-here-now/index.md)

Obtain information about the current state of a channel including a list of unique user IDs
currently subscribed to the channel and the total occupancy count of the channel.

### Parameters

`channels` - The channels to get the 'here now' details of.
    Leave empty for a 'global her now'.

`channelGroups` - The channel groups to get the 'here now' details of.
    Leave empty for a 'global her now'.

`includeState` - Whether the response should include presence state information, if available.
    Defaults to `false`.

`includeUUIDs` - Whether the response should include UUIDs od connected clients.
    Defaults to `true`.