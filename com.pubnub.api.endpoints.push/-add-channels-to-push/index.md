[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.push](../index.md) / [AddChannelsToPush](./index.md)

# AddChannelsToPush

`class AddChannelsToPush : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`, `[`PNPushAddChannelResult`](../../com.pubnub.api.models.consumer.push/-p-n-push-add-channel-result/index.md)`>`

**See Also**

[PubNub.addPushNotificationsOnChannels](../../com.pubnub.api/-pub-nub/add-push-notifications-on-channels.md)

### Properties

| Name | Summary |
|---|---|
| [channels](channels.md) | `val channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [deviceId](device-id.md) | `val deviceId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [environment](environment.md) | `val environment: `[`PNPushEnvironment`](../../com.pubnub.api.enums/-p-n-push-environment/index.md) |
| [pushType](push-type.md) | `val pushType: `[`PNPushType`](../../com.pubnub.api.enums/-p-n-push-type/index.md) |
| [topic](topic.md) | `val topic: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`>): `[`PNPushAddChannelResult`](../../com.pubnub.api.models.consumer.push/-p-n-push-add-channel-result/index.md) |
| [doWork](do-work.md) | `fun doWork(queryParams: <ERROR CLASS><`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`>` |
| [getAffectedChannels](get-affected-channels.md) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [operationType](operation-type.md) | `fun operationType(): PNAddPushNotificationsOnChannelsOperation` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
