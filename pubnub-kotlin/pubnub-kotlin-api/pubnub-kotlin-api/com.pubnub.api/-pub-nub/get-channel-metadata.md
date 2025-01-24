//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[getChannelMetadata](get-channel-metadata.md)

# getChannelMetadata

[common]\
expect abstract fun [getChannelMetadata](get-channel-metadata.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), includeCustom: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false): [GetChannelMetadata](../../com.pubnub.api.endpoints.objects.channel/-get-channel-metadata/index.md)actual abstract fun [getChannelMetadata](get-channel-metadata.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), includeCustom: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)): [GetChannelMetadata](../../com.pubnub.api.endpoints.objects.channel/-get-channel-metadata/index.md)

[jvm]\
actual abstract fun [getChannelMetadata](get-channel-metadata.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), includeCustom: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)): [GetChannelMetadata](../../com.pubnub.api.endpoints.objects.channel/-get-channel-metadata/index.md)

Returns metadata for the specified Channel, optionally including the custom data object for each.

#### Parameters

jvm

| | |
|---|---|
| channel | Channel name. |
| includeCustom | Include respective additional fields in the response. |
