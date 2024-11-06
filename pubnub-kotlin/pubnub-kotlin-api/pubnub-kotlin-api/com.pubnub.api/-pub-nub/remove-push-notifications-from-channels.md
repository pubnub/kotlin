//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[removePushNotificationsFromChannels](remove-push-notifications-from-channels.md)

# removePushNotificationsFromChannels

[common]\
expect abstract fun [removePushNotificationsFromChannels](remove-push-notifications-from-channels.md)(pushType: [PNPushType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-type/index.md), channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, deviceId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), topic: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, environment: [PNPushEnvironment](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-environment/index.md) = PNPushEnvironment.DEVELOPMENT): [RemoveChannelsFromPush](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md)actual abstract fun [removePushNotificationsFromChannels](remove-push-notifications-from-channels.md)(pushType: [PNPushType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-type/index.md), channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, deviceId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), topic: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, environment: [PNPushEnvironment](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-environment/index.md)): [RemoveChannelsFromPush](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md)

[jvm]\
actual abstract fun [removePushNotificationsFromChannels](remove-push-notifications-from-channels.md)(pushType: [PNPushType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-type/index.md), channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, deviceId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), topic: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, environment: [PNPushEnvironment](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-environment/index.md)): [RemoveChannelsFromPush](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md)

Disable push notifications on provided set of channels.

#### Parameters

jvm

| | |
|---|---|
| pushType | Accepted values: FCM, APNS, MPNS, APNS2. @see [PNPushType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-type/index.md) |
| channels | Channels to remove push notifications from. |
| deviceId | The device ID (token) associated with push notifications. |
| environment | Environment within which device should manage list of channels with enabled notifications     (works only if [pushType](remove-push-notifications-from-channels.md) set to [PNPushType.APNS2](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-type/-a-p-n-s2/index.md)). |
| topic | Notifications topic name (usually it is bundle identifier of application for Apple platform).     Required only if pushType set to [PNPushType.APNS2](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-type/-a-p-n-s2/index.md). |
