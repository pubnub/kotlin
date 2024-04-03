package com.pubnub.internal.endpoints.push

import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult
import com.pubnub.internal.EndpointInterface

interface RemoveAllPushChannelsForDeviceInterface : EndpointInterface<PNPushRemoveAllChannelsResult> {
    val pushType: PNPushType
    val deviceId: String
    val environment: PNPushEnvironment
    val topic: String?
}
