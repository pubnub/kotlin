package com.pubnub.api.endpoints.push

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult

/**
 * @see [PubNub.removeAllPushNotificationsFromDeviceWithPushToken]
 */
interface RemoveAllPushChannelsForDevice : com.pubnub.kmp.endpoints.push.RemoveAllPushChannelsForDevice, Endpoint<PNPushRemoveAllChannelsResult>
