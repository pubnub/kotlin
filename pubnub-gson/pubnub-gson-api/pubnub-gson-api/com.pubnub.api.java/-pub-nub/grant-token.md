//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java](../index.md)/[PubNub](index.md)/[grantToken](grant-token.md)

# grantToken

[jvm]\
abstract fun [grantToken](grant-token.md)(): [GrantTokenBuilder](../../com.pubnub.api.java.endpoints.access.builder/-grant-token-builder/index.md)

abstract fun [grantToken](grant-token.md)(ttl: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [GrantTokenBuilder](../../com.pubnub.api.java.endpoints.access.builder/-grant-token-builder/index.md)

This function generates a grant token for PubNub Access Manager (PAM).

Permissions can be applied to any of the three type of resources:

- 
   channels
- 
   channel groups
- 
   uuid - metadata associated with particular UUID

Each type of resource have different set of permissions. To know what's possible for each of them check ChannelGrant, ChannelGroupGrant and UUIDGrant.
