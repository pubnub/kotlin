package com.pubnub.docs.mobilePush

import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.docs.SnippetBase

class RemoveAllMobilePushNotificationsOthers : SnippetBase() {
    private fun removeAllPushNotificationsFromDeviceWithPushToken() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/mobile-push#remove-all-mobile-push-notifications-1

        val pubnub = createPubNub()

        // snippet.removeAllPushNotificationsFromDeviceWithPushToken
        // for FCM/GCM
        pubnub.removeAllPushNotificationsFromDeviceWithPushToken(
            deviceId = "googleDevice",
            pushType = PNPushType.FCM
        ).async { result -> }

        // for APNS2
        pubnub.removeAllPushNotificationsFromDeviceWithPushToken(
            deviceId = "appleDevice",
            pushType = PNPushType.APNS2,
            topic = "myapptopic",
            environment = PNPushEnvironment.DEVELOPMENT
        ).async { result -> }
        // snippet.end
    }
}
