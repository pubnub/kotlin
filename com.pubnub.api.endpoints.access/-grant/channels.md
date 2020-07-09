[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.access](../index.md) / [Grant](index.md) / [channels](./channels.md)

# channels

`var channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`

Specifies the channels on which to grant permissions.
If no channels/channelGroups are specified, then the grant applies to all channels/channelGroups
that have been or will be created for that publish/subscribe key set.

Furthermore, any existing or future grants on specific channels are ignored,
until the all channels grant is revoked.

It's possible to grant permissions to multiple channels simultaneously.
Wildcard notation like a.* can be used to grant access on channels. You can grant one level deep.

* `a.*` - you can grant on this.
* `a.b.*` - grant won't work on this. If you grant on `a.b.*`,
the grant will treat `a.b.*` as a single channel with name `a.b.*`.
