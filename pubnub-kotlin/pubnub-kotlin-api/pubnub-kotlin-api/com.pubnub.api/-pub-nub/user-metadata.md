//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[userMetadata](user-metadata.md)

# userMetadata

[common]\
expect abstract fun [userMetadata](user-metadata.md)(id: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [UserMetadata](../../com.pubnub.api.v2.entities/-user-metadata/index.md)actual abstract fun [userMetadata](user-metadata.md)(id: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [UserMetadata](../../com.pubnub.api.v2.entities/-user-metadata/index.md)

[jvm]\
actual abstract fun [userMetadata](user-metadata.md)(id: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [UserMetadata](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.entities/-user-metadata/index.md)

Create a handle to a [UserMetadata](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.entities/-user-metadata/index.md) object that can be used to obtain a [Subscription](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription/index.md) to user metadata events.

The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server state. It is therefore permitted to use this method whenever a representation of a user metadata is required.

The returned [UserMetadata](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.entities/-user-metadata/index.md) holds a reference to this [PubNub](index.md) instance internally.

#### Return

a [UserMetadata](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.entities/-user-metadata/index.md) instance representing the channel metadata with the given [id](user-metadata.md)

#### Parameters

jvm

| | |
|---|---|
| id | the id of the user. See more in the [documentation](https://www.pubnub.com/docs/general/metadata/users-metadata) |
