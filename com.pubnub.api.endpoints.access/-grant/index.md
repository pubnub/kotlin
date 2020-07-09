[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.access](../index.md) / [Grant](./index.md)

# Grant

`class Grant : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.md)`<`[`AccessManagerGrantPayload`](../../com.pubnub.api.models.server.access_manager/-access-manager-grant-payload/index.md)`>, `[`PNAccessManagerGrantResult`](../../com.pubnub.api.models.consumer.access_manager/-p-n-access-manager-grant-result/index.md)`>`

**See Also**

[PubNub.grant](../../com.pubnub.api/-pub-nub/grant.md)

### Properties

| Name | Summary |
|---|---|
| [authKeys](auth-keys.md) | Specifies authKey to grant permissions to. It's possible to specify multiple auth keys. You can also grant access to a single authKey for multiple channels at the same time.`var authKeys: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [channelGroups](channel-groups.md) | Specifies the channel groups to grant permissions to. If no [channels](channels.md) or [channelGroups](channel-groups.md) are specified, then the grant applies to all channels/channelGroups that have been or will be created for that publish/subscribe key set.`var channelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [channels](channels.md) | Specifies the channels on which to grant permissions. If no channels/channelGroups are specified, then the grant applies to all channels/channelGroups that have been or will be created for that publish/subscribe key set.`var channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [delete](delete.md) | Set to `true` to request the *delete* permission. Defaults to `false`.`var delete: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [manage](manage.md) | Set to `true` to request the *read* permission. Defaults to `false`.`var manage: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [read](read.md) | Set to `true` to request the *read* permission. Defaults to `false`.`var read: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [ttl](ttl.md) | Time in minutes for which granted permissions are valid. Setting ttl to `0` will apply the grant indefinitely, which is also the default behavior.`var ttl: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [write](write.md) | Set to `true` to request the *write* permission. Defaults to `false`.`var write: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.md)`<`[`AccessManagerGrantPayload`](../../com.pubnub.api.models.server.access_manager/-access-manager-grant-payload/index.md)`>>): `[`PNAccessManagerGrantResult`](../../com.pubnub.api.models.consumer.access_manager/-p-n-access-manager-grant-result/index.md)`?` |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.md)`<`[`AccessManagerGrantPayload`](../../com.pubnub.api.models.server.access_manager/-access-manager-grant-payload/index.md)`>>` |
| [getAffectedChannelGroups](get-affected-channel-groups.md) | `fun getAffectedChannelGroups(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [getAffectedChannels](get-affected-channels.md) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [isAuthRequired](is-auth-required.md) | `fun isAuthRequired(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [operationType](operation-type.md) | `fun operationType(): PNAccessManagerGrant` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
