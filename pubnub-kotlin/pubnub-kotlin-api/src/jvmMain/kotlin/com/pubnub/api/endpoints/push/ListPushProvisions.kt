package com.pubnub.api.endpoints.push

import com.pubnub.api.Endpoint
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult

/**
 * @see [PubNub.auditPushChannelProvisions]
 */
actual interface ListPushProvisions : Endpoint<PNPushListProvisionsResult> {
    val pushType: PNPushType
    val deviceId: String
    val topic: String?
    val environment: PNPushEnvironment
}
