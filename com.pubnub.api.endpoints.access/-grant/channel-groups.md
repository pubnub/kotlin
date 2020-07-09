[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.access](../index.md) / [Grant](index.md) / [channelGroups](./channel-groups.md)

# channelGroups

`var channelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`

Specifies the channel groups to grant permissions to.
If no [channels](channels.md) or [channelGroups](./channel-groups.md) are specified, then the grant applies to all channels/channelGroups
that have been or will be created for that publish/subscribe key set.

Furthermore, any existing or future grants on specific [channelGroups](./channel-groups.md) are ignored,
until the all [channelGroups](./channel-groups.md) grant is revoked.

It's possible to grant permissions to multiple [channelGroups](./channel-groups.md) simultaneously.

