[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [grantToken](./grant-token.md)

# grantToken

`fun ~~grantToken~~(ttl: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, meta: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = null, authorizedUUID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ChannelGrant`](../../com.pubnub.api.models.consumer.access_manager.v3/-channel-grant/index.md)`> = emptyList(), channelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ChannelGroupGrant`](../../com.pubnub.api.models.consumer.access_manager.v3/-channel-group-grant/index.md)`> = emptyList(), uuids: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`UUIDGrant`](../../com.pubnub.api.models.consumer.access_manager.v3/-u-u-i-d-grant/index.md)`> = emptyList()): `[`GrantToken`](../../com.pubnub.api.endpoints.access/-grant-token/index.md)
**Deprecated:** DeprecationLevel.WARNING

This function generates a grant token for PubNub Access Manager (PAM).

Permissions can be applied to any of the three type of resources:

* channels
* channel groups
* uuid - metadata associated with particular UUID

Each type of resource have different set of permissions. To know what's possible for each of them
check ChannelGrant, ChannelGroupGrant and UUIDGrant.

### Parameters

`ttl` - Time in minutes for which granted permissions are valid.

`meta` - Additional metadata

`authorizedUUID` - Single uuid which is authorized to use the token to make API requests to PubNub

`channels` - List of all channel grants

`channelGroups` - List of all channel group grants

`uuids` - List of all uuid grants`fun grantToken(ttl: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, meta: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = null, authorizedUserId: `[`UserId`](../-user-id/index.md)`? = null, spacesPermissions: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SpacePermissions`](../../com.pubnub.api.models.consumer.access_manager.sum/-space-permissions/index.md)`> = emptyList(), usersPermissions: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`UserPermissions`](../../com.pubnub.api.models.consumer.access_manager.sum/-user-permissions/index.md)`> = emptyList()): `[`GrantToken`](../../com.pubnub.api.endpoints.access/-grant-token/index.md)

This function generates a grant token for PubNub Access Manager (PAM).

Permissions can be applied to any of the two type of resources:

* spacePermissions
* userPermissions

Each type of resource have different set of permissions. To know what's possible for each of them
check SpacePermissions and UserPermissions.

### Parameters

`ttl` - Time in minutes for which granted permissions are valid.

`meta` - Additional metadata

`authorizedUserId` - Single userId which is authorized to use the token to make API requests to PubNub

`spacesPermissions` - List of all space grants

`usersPermissions` - List of all userId grants