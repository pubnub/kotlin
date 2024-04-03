package com.pubnub.internal.endpoints.push

import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult
import com.pubnub.internal.EndpointInterface

interface ListPushProvisionsInterface : EndpointInterface<PNPushListProvisionsResult> {
    val pushType: PNPushType
    val deviceId: String
    val topic: String?
    val environment: PNPushEnvironment
}
