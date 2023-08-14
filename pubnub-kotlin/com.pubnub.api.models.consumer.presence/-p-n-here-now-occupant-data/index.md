//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.presence](../index.md)/[PNHereNowOccupantData](index.md)

# PNHereNowOccupantData

[jvm]\
class [PNHereNowOccupantData](index.md)

Wrapper class representing a UUID (user) within the means of [PubNub.hereNow](../../com.pubnub.api/-pub-nub/here-now.md) calls.

## Properties

| Name | Summary |
|---|---|
| [state](state.md) | [jvm]<br>val [state](state.md): JsonElement? = null<br>Presence State of the user if requested via [HereNow.includeState](../../com.pubnub.api.endpoints.presence/-here-now/include-state.md). |
| [uuid](uuid.md) | [jvm]<br>val [uuid](uuid.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>UUID of the user if requested via [HereNow.includeUUIDs](../../com.pubnub.api.endpoints.presence/-here-now/include-u-u-i-ds.md). |
