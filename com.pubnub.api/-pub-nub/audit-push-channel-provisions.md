[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [auditPushChannelProvisions](./audit-push-channel-provisions.md)

# auditPushChannelProvisions

`fun auditPushChannelProvisions(pushType: `[`PNPushType`](../../com.pubnub.api.enums/-p-n-push-type/index.md)`, deviceId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, topic: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, environment: `[`PNPushEnvironment`](../../com.pubnub.api.enums/-p-n-push-environment/index.md)` = PNPushEnvironment.DEVELOPMENT): `[`ListPushProvisions`](../../com.pubnub.api.endpoints.push/-list-push-provisions/index.md)

Request a list of all channels on which push notifications have been enabled using specified [ListPushProvisions.deviceId](../../com.pubnub.api.endpoints.push/-list-push-provisions/device-id.md).

### Parameters

`pushType` - Accepted values: FCM, APNS, MPNS, APNS2. @see [PNPushType](../../com.pubnub.api.enums/-p-n-push-type/index.md)

`deviceId` - The device ID (token) to associate with push notifications.

`environment` - Environment within which device should manage list of channels with enabled notifications
    (works only if [pushType](audit-push-channel-provisions.md#com.pubnub.api.PubNub$auditPushChannelProvisions(com.pubnub.api.enums.PNPushType, kotlin.String, kotlin.String, com.pubnub.api.enums.PNPushEnvironment)/pushType) set to [PNPushType.APNS2](../../com.pubnub.api.enums/-p-n-push-type/-a-p-n-s2.md)).

`topic` - Notifications topic name (usually it is bundle identifier of application for Apple platform).
    Required only if pushType set to [PNPushType.APNS2](../../com.pubnub.api.enums/-p-n-push-type/-a-p-n-s2.md).