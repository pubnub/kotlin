//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[setUUIDMetadata](set-u-u-i-d-metadata.md)

# setUUIDMetadata

[common]\
expect abstract fun [setUUIDMetadata](set-u-u-i-d-metadata.md)(uuid: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, externalId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, profileUrl: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, email: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, custom: [CustomObject](../../com.pubnub.kmp/-custom-object/index.md)? = null, includeCustom: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, type: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, status: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, ifMatchesEtag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null): [SetUUIDMetadata](../../com.pubnub.api.endpoints.objects.uuid/-set-u-u-i-d-metadata/index.md)actual abstract fun [setUUIDMetadata](set-u-u-i-d-metadata.md)(uuid: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, externalId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, profileUrl: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, email: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, custom: [CustomObject](../../com.pubnub.kmp/-custom-object/index.md)?, includeCustom: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), type: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, status: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, ifMatchesEtag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?): [SetUUIDMetadata](../../com.pubnub.api.endpoints.objects.uuid/-set-u-u-i-d-metadata/index.md)

[jvm]\
actual abstract fun [setUUIDMetadata](set-u-u-i-d-metadata.md)(uuid: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, externalId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, profileUrl: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, email: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, custom: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?, includeCustom: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), type: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, status: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, ifMatchesEtag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?): [SetUUIDMetadata](../../com.pubnub.api.endpoints.objects.uuid/-set-u-u-i-d-metadata/index.md)

Set metadata for a UUID in the database, optionally including the custom data object for each.

#### Parameters

jvm

| | |
|---|---|
| uuid | Unique user identifier. If not supplied then current user’s uuid is used. |
| name | Display name for the user. Maximum 200 characters. |
| externalId | User's identifier in an external system |
| profileUrl | The URL of the user's profile picture |
| email | The user's email address. Maximum 80 characters. |
| custom | Object with supported data types. |
| includeCustom | Include respective additional fields in the response. |
| ifMatchesEtag | Optional entity tag from a previously received `PNUUIDMetadata`. The request will fail if this parameter is specified and the ETag value on the server doesn't match. |
