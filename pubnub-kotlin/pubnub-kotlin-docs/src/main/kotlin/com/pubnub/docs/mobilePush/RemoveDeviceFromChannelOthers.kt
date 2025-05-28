package com.pubnub.docs.mobilePush

import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.docs.SnippetBase

class RemoveDeviceFromChannelOthers : SnippetBase() {
    private fun removePushNotificationsFromChannels() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/mobile-push#remove-device-from-channel-1
        val pubnub = createPubNub()

        // snippet.removePushNotificationsFromChannels
        // for FCM/GCM
        pubnub.removePushNotificationsFromChannels(
            deviceId = "googleDevice",
            channels = listOf("ch1", "ch2", "ch3"),
            pushType = PNPushType.FCM
        ).async { result -> }

        // for APNS2
        pubnub.removePushNotificationsFromChannels(
            deviceId = "appleDevice",
            channels = listOf("ch1", "ch2", "ch3"),
            pushType = PNPushType.APNS2,
            topic = "myapptopic",
            environment = PNPushEnvironment.DEVELOPMENT
        ).async { result -> }
        // snippet.end
    }
}
