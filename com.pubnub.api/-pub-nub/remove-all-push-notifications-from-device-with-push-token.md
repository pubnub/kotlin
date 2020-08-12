[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [removeAllPushNotificationsFromDeviceWithPushToken](./remove-all-push-notifications-from-device-with-push-token.md)

# removeAllPushNotificationsFromDeviceWithPushToken

`fun removeAllPushNotificationsFromDeviceWithPushToken(pushType: `[`PNPushType`](../../com.pubnub.api.enums/-p-n-push-type/index.md)`, deviceId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, topic: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, environment: `[`PNPushEnvironment`](../../com.pubnub.api.enums/-p-n-push-environment/index.md)` = PNPushEnvironment.DEVELOPMENT): `[`RemoveAllPushChannelsForDevice`](../../com.pubnub.api.endpoints.push/-remove-all-push-channels-for-device/index.md)

Disable push notifications from all channels registered with the specified [RemoveAllPushChannelsForDevice.deviceId](../../com.pubnub.api.endpoints.push/-remove-all-push-channels-for-device/device-id.md).

### Parameters

`pushType` - Accepted values: FCM, APNS, MPNS, APNS2. @see [PNPushType](../../com.pubnub.api.enums/-p-n-push-type/index.md)

`deviceId` - The device ID (token) to associate with push notifications.

`environment` - Environment within which device should manage list of channels with enabled notifications
    (works only if [pushType](remove-all-push-notifications-from-device-with-push-token.md#com.pubnub.api.PubNub$removeAllPushNotificationsFromDeviceWithPushToken(com.pubnub.api.enums.PNPushType, kotlin.String, kotlin.String, com.pubnub.api.enums.PNPushEnvironment)/pushType) set to [PNPushType.APNS2](../../com.pubnub.api.enums/-p-n-push-type/-a-p-n-s2.md)).

`topic` - Notifications topic name (usually it is bundle identifier of application for Apple platform).
    Required only if pushType set to [PNPushType.APNS2](../../com.pubnub.api.enums/-p-n-push-type/-a-p-n-s2.md).