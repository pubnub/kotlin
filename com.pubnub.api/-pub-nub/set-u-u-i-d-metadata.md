[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [setUUIDMetadata](./set-u-u-i-d-metadata.md)

# setUUIDMetadata

`fun setUUIDMetadata(uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, externalId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, profileUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, email: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, custom: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = null, includeCustom: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, status: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`SetUUIDMetadata`](../../com.pubnub.api.endpoints.objects.uuid/-set-u-u-i-d-metadata/index.md)

Set metadata for a UUID in the database, optionally including the custom data object for each.

### Parameters

`uuid` - Unique user identifier. If not supplied then current user’s uuid is used.

`name` - Display name for the user. Maximum 200 characters.

`externalId` - User's identifier in an external system

`profileUrl` - The URL of the user's profile picture

`email` - The user's email address. Maximum 80 characters.

`custom` - Object with supported data types.

`includeCustom` - Include respective additional fields in the response.