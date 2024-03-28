//[pubnub-core-api](../../../index.md)/[com.pubnub.api.models.consumer.presence](../index.md)/[PNHereNowChannelData](index.md)

# PNHereNowChannelData

[jvm]\
class [PNHereNowChannelData](index.md)(val channelName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val occupancy: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), var occupants: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNHereNowOccupantData](../-p-n-here-now-occupant-data/index.md)&gt; = emptyList())

Wrapper class representing 'here now' data for a given channel.

## Constructors

| | |
|---|---|
| [PNHereNowChannelData](-p-n-here-now-channel-data.md) | [jvm]<br>constructor(channelName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), occupancy: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), occupants: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNHereNowOccupantData](../-p-n-here-now-occupant-data/index.md)&gt; = emptyList()) |

## Properties

| Name | Summary |
|---|---|
| [channelName](channel-name.md) | [jvm]<br>val [channelName](channel-name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The channel name. |
| [occupancy](occupancy.md) | [jvm]<br>val [occupancy](occupancy.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Total number of UUIDs currently in the channel. |
| [occupants](occupants.md) | [jvm]<br>var [occupants](occupants.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNHereNowOccupantData](../-p-n-here-now-occupant-data/index.md)&gt;<br>List of [PNHereNowOccupantData](../-p-n-here-now-occupant-data/index.md) (users) currently in the channel. |
