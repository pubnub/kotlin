//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.presence](../index.md)/[PNHereNowResult](index.md)

# PNHereNowResult

[jvm]\
class [PNHereNowResult](index.md)

Result of the [PubNub.hereNow](../../com.pubnub.api/-pub-nub/here-now.md) operation.

## Properties

| Name | Summary |
|---|---|
| [channels](channels.md) | [jvm]<br>val [channels](channels.md): [HashMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [PNHereNowChannelData](../-p-n-here-now-channel-data/index.md)&gt;<br>A map with values of [PNHereNowChannelData](../-p-n-here-now-channel-data/index.md) for each channel. |
| [totalChannels](total-channels.md) | [jvm]<br>val [totalChannels](total-channels.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Total number channels matching the associated [PubNub.hereNow](../../com.pubnub.api/-pub-nub/here-now.md) call. |
| [totalOccupancy](total-occupancy.md) | [jvm]<br>val [totalOccupancy](total-occupancy.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Total occupancy matching the associated [PubNub.hereNow](../../com.pubnub.api/-pub-nub/here-now.md) call. |
