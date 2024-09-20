package com.pubnub.api.endpoints.push

import com.pubnub.api.Endpoint
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult

/**
 * @see [PubNub.addPushNotificationsOnChannels]
 */
actual interface AddChannelsToPush : Endpoint<PNPushAddChannelResult> {
    val pushType: PNPushType
    val channels: List<String>
    val deviceId: String
    val topic: String?
    val environment: PNPushEnvironment
}
