[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.push](../index.md) / [RemoveAllPushChannelsForDevice](./index.md)

# RemoveAllPushChannelsForDevice

`class RemoveAllPushChannelsForDevice : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`, `[`PNPushRemoveAllChannelsResult`](../../com.pubnub.api.models.consumer.push/-p-n-push-remove-all-channels-result/index.md)`>`

**See Also**

[PubNub.removeAllPushNotificationsFromDeviceWithPushToken](../../com.pubnub.api/-pub-nub/remove-all-push-notifications-from-device-with-push-token.md)

### Properties

| Name | Summary |
|---|---|
| [deviceId](device-id.md) | The device ID (token) to associate with push notifications.`lateinit var deviceId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [environment](environment.md) | Environment within which device should manage list of channels with enabled notifications (works only if [pushType](push-type.md) set to [PNPushType.APNS2](../../com.pubnub.api.enums/-p-n-push-type/-a-p-n-s2.md)).`var environment: `[`PNPushEnvironment`](../../com.pubnub.api.enums/-p-n-push-environment/index.md) |
| [pushType](push-type.md) | Accepted values: FCM, APNS, MPNS, APNS2.`lateinit var pushType: `[`PNPushType`](../../com.pubnub.api.enums/-p-n-push-type/index.md) |
| [topic](topic.md) | Notifications topic name (usually it is bundle identifier of application for Apple platform). Required only if pushType set to [PNPushType.APNS2](../../com.pubnub.api.enums/-p-n-push-type/-a-p-n-s2.md).`lateinit var topic: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`>): `[`PNPushRemoveAllChannelsResult`](../../com.pubnub.api.models.consumer.push/-p-n-push-remove-all-channels-result/index.md)`?` |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`>` |
| [operationType](operation-type.md) | `fun operationType(): PNRemoveAllPushNotificationsOperation` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
