[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [presence](./presence.md)

# presence

`fun presence(channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList(), channelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList(), connected: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Track the online and offline status of users and devices in real time and store custom state information.
When you have Presence enabled, PubNub automatically creates a presence channel for each channel.

Subscribing to a presence channel or presence channel group will only return presence events

### Parameters

`channels` - Channels to subscribe/unsubscribe. Either `channel` or [channelGroups](presence.md#com.pubnub.api.PubNub$presence(kotlin.collections.List((kotlin.String)), kotlin.collections.List((kotlin.String)), kotlin.Boolean)/channelGroups) are required.

`channelGroups` - Channel groups to subscribe/unsubscribe. Either `channelGroups` or [channels](presence.md#com.pubnub.api.PubNub$presence(kotlin.collections.List((kotlin.String)), kotlin.collections.List((kotlin.String)), kotlin.Boolean)/channels) are required.