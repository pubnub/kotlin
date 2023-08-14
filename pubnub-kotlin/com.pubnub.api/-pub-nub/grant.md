//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[grant](grant.md)

# grant

[jvm]\
fun [grant](grant.md)(read: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, write: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, manage: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, delete: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, ttl: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = -1, authKeys: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptyList(), channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptyList(), channelGroups: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptyList()): [Grant](../../com.pubnub.api.endpoints.access/-grant/index.md)

This function establishes access permissions for PubNub Access Manager (PAM) by setting the `read` or `write` attribute to `true`. A grant with `read` or `write` set to `false` (or not included) will revoke any previous grants with `read` or `write` set to `true`.

Permissions can be applied to any one of three levels:

- 
   Application level privileges are based on `subscribeKey` applying to all associated channels.
- 
   Channel level privileges are based on a combination of `subscribeKey` and `channel` name.
- 
   User level privileges are based on the combination of `subscribeKey`, `channel`, and `auth_key`.

## Parameters

jvm

| | |
|---|---|
| read | Set to `true` to request the *read* permission. Defaults to `false`. |
| write | Set to `true` to request the *write* permission. Defaults to `false`. |
| manage | Set to `true` to request the *read* permission. Defaults to `false`. |
| delete | Set to `true` to request the *delete* permission. Defaults to `false`. |
| ttl | Time in minutes for which granted permissions are valid.     Setting ttl to `0` will apply the grant indefinitely, which is also the default behavior. |
| authKeys | Specifies authKey to grant permissions to. It's possible to specify multiple auth keys.     You can also grant access to a single authKey for multiple channels at the same time. |
| channels | Specifies the channels on which to grant permissions.     If no channels/channelGroups are specified, then the grant applies to all channels/channelGroups     that have been or will be created for that publish/subscribe key set.<br>```kotlin     Furthermore, any existing or future grants on specific channels are ignored,     until the all channels grant is revoked.<br>    It's possible to grant permissions to multiple channels simultaneously.     Wildcard notation like a.* can be used to grant access on channels. You can grant one level deep.     - `a.*` - you can grant on this.     - `a.b.*` - grant won't work on this. If you grant on `a.b.*`,       the grant will treat `a.b.*` as a single channel with name `a.b.*`. ``` |
| channelGroups | Specifies the channel groups to grant permissions to.     If no [channels](grant.md) or [channelGroups](grant.md) are specified, then the grant applies to all channels/channelGroups     that have been or will be created for that publish/subscribe key set.<br>```kotlin     Furthermore, any existing or future grants on specific [channelGroups] are ignored,     until the all [channelGroups] grant is revoked.<br>    It's possible to grant permissions to multiple [channelGroups] simultaneously. ``` |
