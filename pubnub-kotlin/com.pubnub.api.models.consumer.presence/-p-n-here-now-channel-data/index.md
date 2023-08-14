//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.presence](../index.md)/[PNHereNowChannelData](index.md)

# PNHereNowChannelData

[jvm]\
class [PNHereNowChannelData](index.md)

Wrapper class representing 'here now' data for a given channel.

## Properties

| Name | Summary |
|---|---|
| [channelName](channel-name.md) | [jvm]<br>val [channelName](channel-name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The channel name. |
| [occupancy](occupancy.md) | [jvm]<br>val [occupancy](occupancy.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Total number of UUIDs currently in the channel. |
| [occupants](occupants.md) | [jvm]<br>var [occupants](occupants.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNHereNowOccupantData](../-p-n-here-now-occupant-data/index.md)&gt;<br>List of [PNHereNowOccupantData](../-p-n-here-now-occupant-data/index.md) (users) currently in the channel. |
