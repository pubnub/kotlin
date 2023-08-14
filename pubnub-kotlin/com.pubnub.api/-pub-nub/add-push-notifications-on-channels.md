//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[addPushNotificationsOnChannels](add-push-notifications-on-channels.md)

# addPushNotificationsOnChannels

[jvm]\
fun [addPushNotificationsOnChannels](add-push-notifications-on-channels.md)(pushType: [PNPushType](../../com.pubnub.api.enums/-p-n-push-type/index.md), channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, deviceId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), topic: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, environment: [PNPushEnvironment](../../com.pubnub.api.enums/-p-n-push-environment/index.md) = PNPushEnvironment.DEVELOPMENT): [AddChannelsToPush](../../com.pubnub.api.endpoints.push/-add-channels-to-push/index.md)

Enable push notifications on provided set of channels.

## Parameters

jvm

| | |
|---|---|
| pushType | Accepted values: FCM, APNS, MPNS, APNS2.     @see [PNPushType](../../com.pubnub.api.enums/-p-n-push-type/index.md) |
| channels | Channels to add push notifications to. |
| deviceId | The device ID (token) to associate with push notifications. |
| environment | Environment within which device should manage list of channels with enabled notifications     (works only if [pushType](add-push-notifications-on-channels.md) set to [PNPushType.APNS2](../../com.pubnub.api.enums/-p-n-push-type/-a-p-n-s2/index.md)). |
| topic | Notifications topic name (usually it is bundle identifier of application for Apple platform).     Required only if pushType set to [PNPushType.APNS2](../../com.pubnub.api.enums/-p-n-push-type/-a-p-n-s2/index.md). |
