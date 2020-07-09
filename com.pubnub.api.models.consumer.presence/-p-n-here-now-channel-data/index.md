[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.presence](../index.md) / [PNHereNowChannelData](./index.md)

# PNHereNowChannelData

`class PNHereNowChannelData`

Wrapper class representing 'here now' data for a given channel.

### Properties

| Name | Summary |
|---|---|
| [channelName](channel-name.md) | The channel name.`val channelName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [occupancy](occupancy.md) | Total number of UUIDs currently in the channel.`val occupancy: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [occupants](occupants.md) | List of [PNHereNowOccupantData](../-p-n-here-now-occupant-data/index.md) (users) currently in the channel.`var occupants: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`PNHereNowOccupantData`](../-p-n-here-now-occupant-data/index.md)`>` |
