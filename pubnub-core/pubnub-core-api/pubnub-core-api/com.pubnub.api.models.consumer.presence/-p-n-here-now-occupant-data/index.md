//[pubnub-core-api](../../../index.md)/[com.pubnub.api.models.consumer.presence](../index.md)/[PNHereNowOccupantData](index.md)

# PNHereNowOccupantData

[jvm]\
class [PNHereNowOccupantData](index.md)(val uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val state: JsonElement? = null)

Wrapper class representing a UUID (user) within the means of HereNow calls.

## Constructors

| | |
|---|---|
| [PNHereNowOccupantData](-p-n-here-now-occupant-data.md) | [jvm]<br>constructor(uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), state: JsonElement? = null) |

## Properties

| Name | Summary |
|---|---|
| [state](state.md) | [jvm]<br>val [state](state.md): JsonElement? = null<br>Presence State of the user if requested via HereNow.includeState. |
| [uuid](uuid.md) | [jvm]<br>val [uuid](uuid.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>UUID of the user if requested via HereNow.includeUUIDs. |
