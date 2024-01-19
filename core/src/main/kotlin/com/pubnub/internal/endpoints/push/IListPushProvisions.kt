package com.pubnub.internal.endpoints.push

import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType

interface IListPushProvisions {
    val pushType: PNPushType
    val deviceId: String
    val topic: String?
    val environment: PNPushEnvironment
}
