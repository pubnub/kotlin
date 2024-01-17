package com.pubnub.internal.endpoints.push

import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType

interface IRemoveAllPushChannelsForDevice {
    val pushType: PNPushType
    val deviceId: String
    val environment: PNPushEnvironment
    val topic: String?
}
