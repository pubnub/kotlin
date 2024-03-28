//[pubnub-core-api](../../../index.md)/[com.pubnub.api](../index.md)/[BasePubNub](index.md)/[userMetadata](user-metadata.md)

# userMetadata

[jvm]\
abstract fun [userMetadata](user-metadata.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [UserMetadata](index.md)

Create a handle to a [UserMetadata](index.md) object that can be used to obtain a [Subscription](index.md) to user metadata events.

The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server state. It is therefore permitted to use this method whenever a representation of a user metadata is required.

The returned [UserMetadata](index.md) holds a reference to this PubNub instance internally.

#### Return

a [UserMetadata](index.md) instance representing the channel metadata with the given [id](user-metadata.md)

#### Parameters

jvm

| | |
|---|---|
| id | the id of the user. See more in the [documentation](https://www.pubnub.com/docs/general/metadata/users-metadata) |
