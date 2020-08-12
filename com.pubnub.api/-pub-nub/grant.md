[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [grant](./grant.md)

# grant

`fun grant(read: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, write: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, manage: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, delete: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, ttl: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = -1, authKeys: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList(), channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList(), channelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList()): `[`Grant`](../../com.pubnub.api.endpoints.access/-grant/index.md)

This function establishes access permissions for PubNub Access Manager (PAM) by setting the `read` or `write`
attribute to `true`.
A grant with `read` or `write` set to `false` (or not included) will revoke any previous grants
with `read` or `write` set to `true`.

Permissions can be applied to any one of three levels:

* Application level privileges are based on `subscribeKey` applying to all associated channels.
* Channel level privileges are based on a combination of `subscribeKey` and `channel` name.
* User level privileges are based on the combination of `subscribeKey`, `channel`, and `auth_key`.

### Parameters

`read` - Set to `true` to request the *read* permission. Defaults to `false`.

`write` - Set to `true` to request the *write* permission. Defaults to `false`.

`manage` - Set to `true` to request the *read* permission. Defaults to `false`.

`delete` - Set to `true` to request the *delete* permission. Defaults to `false`.

`ttl` - Time in minutes for which granted permissions are valid.
    Setting ttl to `0` will apply the grant indefinitely, which is also the default behavior.

`authKeys` - Specifies authKey to grant permissions to. It's possible to specify multiple auth keys.
    You can also grant access to a single authKey for multiple channels at the same time.

`channels` - Specifies the channels on which to grant permissions.
    If no channels/channelGroups are specified, then the grant applies to all channels/channelGroups
    that have been or will be created for that publish/subscribe key set.

`channelGroups` - Specifies the channel groups to grant permissions to.
    If no [channels](grant.md#com.pubnub.api.PubNub$grant(kotlin.Boolean, kotlin.Boolean, kotlin.Boolean, kotlin.Boolean, kotlin.Int, kotlin.collections.List((kotlin.String)), kotlin.collections.List((kotlin.String)), kotlin.collections.List((kotlin.String)))/channels) or [channelGroups](grant.md#com.pubnub.api.PubNub$grant(kotlin.Boolean, kotlin.Boolean, kotlin.Boolean, kotlin.Boolean, kotlin.Int, kotlin.collections.List((kotlin.String)), kotlin.collections.List((kotlin.String)), kotlin.collections.List((kotlin.String)))/channelGroups) are specified, then the grant applies to all channels/channelGroups
    that have been or will be created for that publish/subscribe key set.