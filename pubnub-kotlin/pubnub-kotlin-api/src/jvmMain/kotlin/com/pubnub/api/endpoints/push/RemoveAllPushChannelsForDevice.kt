package com.pubnub.api.endpoints.push

import com.pubnub.api.Endpoint
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult

/**
 * @see [PubNub.removeAllPushNotificationsFromDeviceWithPushToken]
 */
actual interface RemoveAllPushChannelsForDevice : Endpoint<PNPushRemoveAllChannelsResult> {
    val pushType: PNPushType
    val deviceId: String
    val environment: PNPushEnvironment
    val topic: String?
}
