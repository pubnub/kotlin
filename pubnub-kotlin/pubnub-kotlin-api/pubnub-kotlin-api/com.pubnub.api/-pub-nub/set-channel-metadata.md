//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[setChannelMetadata](set-channel-metadata.md)

# setChannelMetadata

[common]\
expect abstract fun [setChannelMetadata](set-channel-metadata.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, description: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, custom: [CustomObject](../../com.pubnub.kmp/-custom-object/index.md)? = null, includeCustom: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, type: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, status: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, ifMatchesEtag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null): [SetChannelMetadata](../../com.pubnub.api.endpoints.objects.channel/-set-channel-metadata/index.md)actual abstract fun [setChannelMetadata](set-channel-metadata.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, description: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, custom: [CustomObject](../../com.pubnub.kmp/-custom-object/index.md)?, includeCustom: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), type: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, status: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, ifMatchesEtag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?): [SetChannelMetadata](../../com.pubnub.api.endpoints.objects.channel/-set-channel-metadata/index.md)

[jvm]\
actual abstract fun [setChannelMetadata](set-channel-metadata.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, description: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, custom: [CustomObject](../../com.pubnub.kmp/-custom-object/index.md)?, includeCustom: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), type: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, status: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, ifMatchesEtag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?): [SetChannelMetadata](../../com.pubnub.api.endpoints.objects.channel/-set-channel-metadata/index.md)

Set metadata for a Channel in the database, optionally including the custom data object for each.

#### Parameters

jvm

| | |
|---|---|
| channel | Channel name. |
| name | Name of a channel. |
| description | Description of a channel. |
| custom | Object with supported data types. |
| includeCustom | Include respective additional fields in the response. |
| ifMatchesEtag | Optional entity tag from a previously received `PNChannelMetadata`. The request will fail if this parameter is specified and the ETag value on the server doesn't match. |
