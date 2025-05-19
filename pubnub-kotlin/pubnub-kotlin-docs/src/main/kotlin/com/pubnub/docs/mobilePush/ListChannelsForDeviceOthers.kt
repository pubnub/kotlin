package com.pubnub.docs.mobilePush

import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.docs.SnippetBase

class ListChannelsForDeviceOthers : SnippetBase() {
    private fun auditPushChannelProvisionsBasic() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/mobile-push#list-channels-for-device-1

        val pubnub = createPubNub()

        // snippet.auditPushChannelProvisions
        // for FCM/GCM
        pubnub.auditPushChannelProvisions(
            pushType = PNPushType.FCM,
            deviceId = "googleDevice"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }

        // for APNS2
        pubnub.auditPushChannelProvisions(
            pushType = PNPushType.APNS2,
            deviceId = "appleDevice",
            topic = "myapptopic",
            environment = PNPushEnvironment.DEVELOPMENT
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end
    }
}
