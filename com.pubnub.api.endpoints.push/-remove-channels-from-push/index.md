[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.push](../index.md) / [RemoveChannelsFromPush](./index.md)

# RemoveChannelsFromPush

`class RemoveChannelsFromPush : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`, `[`PNPushRemoveChannelResult`](../../com.pubnub.api.models.consumer.push/-p-n-push-remove-channel-result/index.md)`>`

**See Also**

[PubNub.removePushNotificationsFromChannels](../../com.pubnub.api/-pub-nub/remove-push-notifications-from-channels.md)

### Properties

| Name | Summary |
|---|---|
| [channels](channels.md) | Channels to remove push notifications from.`lateinit var channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [deviceId](device-id.md) | The device ID (token) associated with push notifications.`lateinit var deviceId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [environment](environment.md) | Environment within which device should manage list of channels with enabled notifications (works only if [pushType](push-type.md) set to [PNPushType.APNS2](../../com.pubnub.api.enums/-p-n-push-type/-a-p-n-s2.md)).`var environment: `[`PNPushEnvironment`](../../com.pubnub.api.enums/-p-n-push-environment/index.md) |
| [pushType](push-type.md) | Accepted values: FCM, APNS, MPNS, APNS2.`lateinit var pushType: `[`PNPushType`](../../com.pubnub.api.enums/-p-n-push-type/index.md) |
| [topic](topic.md) | Notifications topic name (usually it is bundle identifier of application for Apple platform). Required only if pushType set to [PNPushType.APNS2](../../com.pubnub.api.enums/-p-n-push-type/-a-p-n-s2.md).`lateinit var topic: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`>): `[`PNPushRemoveChannelResult`](../../com.pubnub.api.models.consumer.push/-p-n-push-remove-channel-result/index.md)`?` |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`>` |
| [getAffectedChannels](get-affected-channels.md) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [operationType](operation-type.md) | `fun operationType(): PNRemovePushNotificationsFromChannelsOperation` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
