//[pubnub-kotlin-docs](../../index.md)/[com.pubnub.docs.mobilePush](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [ListChannelsForDeviceOthers](-list-channels-for-device-others/index.md) | [jvm]<br>class [ListChannelsForDeviceOthers](-list-channels-for-device-others/index.md) : [SnippetBase](../com.pubnub.docs/-snippet-base/index.md) |
| [RemoveAllMobilePushNotificationsOthers](-remove-all-mobile-push-notifications-others/index.md) | [jvm]<br>class [RemoveAllMobilePushNotificationsOthers](-remove-all-mobile-push-notifications-others/index.md) : [SnippetBase](../com.pubnub.docs/-snippet-base/index.md) |
| [RemoveDeviceFromChannelOthers](-remove-device-from-channel-others/index.md) | [jvm]<br>class [RemoveDeviceFromChannelOthers](-remove-device-from-channel-others/index.md) : [SnippetBase](../com.pubnub.docs/-snippet-base/index.md) |

## Functions

| Name | Summary |
|---|---|
| [listPushNotificationChannels](list-push-notification-channels.md) | [jvm]<br>fun [listPushNotificationChannels](list-push-notification-channels.md)(pubnub: [PubNub](../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api/-pub-nub/index.md), fcmToken: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), apnsToken: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html))<br>Demonstrates how to list channels where a device is registered for push notifications |
| [main](main.md) | [jvm]<br>fun [main](main.md)()<br>This example demonstrates how to use Mobile Push Notifications in the PubNub Kotlin SDK. |
| [publishMessageWithPushNotification](publish-message-with-push-notification.md) | [jvm]<br>fun [publishMessageWithPushNotification](publish-message-with-push-notification.md)(pubnub: [PubNub](../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api/-pub-nub/index.md))<br>Demonstrates how to publish a message with push notification payload |
| [registerDeviceForPushNotifications](register-device-for-push-notifications.md) | [jvm]<br>fun [registerDeviceForPushNotifications](register-device-for-push-notifications.md)(pubnub: [PubNub](../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api/-pub-nub/index.md), fcmToken: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), apnsToken: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html))<br>Demonstrates how to register a device to receive push notifications on specific channels |
| [unregisterDeviceFromPushNotifications](unregister-device-from-push-notifications.md) | [jvm]<br>fun [unregisterDeviceFromPushNotifications](unregister-device-from-push-notifications.md)(pubnub: [PubNub](../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api/-pub-nub/index.md), fcmToken: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), apnsToken: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html))<br>Demonstrates how to remove a device from push notification channels |
