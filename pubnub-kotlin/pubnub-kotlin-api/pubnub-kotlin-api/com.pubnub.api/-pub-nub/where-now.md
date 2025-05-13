//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[whereNow](where-now.md)

# whereNow

[common]\
expect abstract fun [whereNow](where-now.md)(uuid: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = configuration.userId.value): [WhereNow](../../com.pubnub.api.endpoints.presence/-where-now/index.md)actual abstract fun [whereNow](where-now.md)(uuid: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [WhereNow](../../com.pubnub.api.endpoints.presence/-where-now/index.md)

[jvm]\
actual abstract fun [whereNow](where-now.md)(uuid: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [WhereNow](../../com.pubnub.api.endpoints.presence/-where-now/index.md)

Obtain information about the current list of channels to which a UUID is subscribed to.

#### Parameters

jvm

| | |
|---|---|
| uuid | UUID of the user to get its current channel subscriptions. Defaults to the UUID of the client. |

#### See also

| |
|---|
| [PNConfiguration.uuid](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration/uuid.md) |
