//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.models.consumer.presence](../index.md)/[PNHereNowOccupantData](index.md)

# PNHereNowOccupantData

[common]\
class [PNHereNowOccupantData](index.md)(val uuid: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val state: [JsonElement](../../com.pubnub.api/-json-element/index.md)? = null)

Wrapper class representing a UUID (user) within the means of HereNow calls.

## Constructors

| | |
|---|---|
| [PNHereNowOccupantData](-p-n-here-now-occupant-data.md) | [common]<br>constructor(uuid: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), state: [JsonElement](../../com.pubnub.api/-json-element/index.md)? = null) |

## Properties

| Name | Summary |
|---|---|
| [state](state.md) | [common]<br>val [state](state.md): [JsonElement](../../com.pubnub.api/-json-element/index.md)? = null<br>Presence State of the user if requested via HereNow.includeState. |
| [uuid](uuid.md) | [common]<br>val [uuid](uuid.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>UUID of the user if requested via HereNow.includeUUIDs. |
