//[pubnub-core-api](../../index.md)/[com.pubnub.api.models.consumer.presence](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [PNGetStateResult](-p-n-get-state-result/index.md) | [jvm]<br>class [PNGetStateResult](-p-n-get-state-result/index.md)(val stateByUUID: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), JsonElement&gt;)<br>Result of the GetPresenceState operation. |
| [PNHereNowChannelData](-p-n-here-now-channel-data/index.md) | [jvm]<br>class [PNHereNowChannelData](-p-n-here-now-channel-data/index.md)(val channelName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val occupancy: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), var occupants: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNHereNowOccupantData](-p-n-here-now-occupant-data/index.md)&gt; = emptyList())<br>Wrapper class representing 'here now' data for a given channel. |
| [PNHereNowOccupantData](-p-n-here-now-occupant-data/index.md) | [jvm]<br>class [PNHereNowOccupantData](-p-n-here-now-occupant-data/index.md)(val uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val state: JsonElement? = null)<br>Wrapper class representing a UUID (user) within the means of HereNow calls. |
| [PNHereNowResult](-p-n-here-now-result/index.md) | [jvm]<br>class [PNHereNowResult](-p-n-here-now-result/index.md)(val totalChannels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val totalOccupancy: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val channels: [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [PNHereNowChannelData](-p-n-here-now-channel-data/index.md)&gt; = mutableMapOf())<br>Result of the HereNow operation. |
| [PNSetStateResult](-p-n-set-state-result/index.md) | [jvm]<br>class [PNSetStateResult](-p-n-set-state-result/index.md)(val state: JsonElement)<br>Result of the PubNubImpl.setPresenceState operation. |
