//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[whereNow](where-now.md)

# whereNow

[jvm]\
abstract fun [whereNow](where-now.md)(uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = configuration.userId.value): [WhereNow](../../com.pubnub.api.endpoints.presence/-where-now/index.md)

Obtain information about the current list of channels to which a UUID is subscribed to.

#### Parameters

jvm

| | |
|---|---|
| uuid | UUID of the user to get its current channel subscriptions. Defaults to the UUID of the client. |

#### See also

| |
|---|
| [PNConfiguration.uuid](../-p-n-configuration/uuid.md) |
