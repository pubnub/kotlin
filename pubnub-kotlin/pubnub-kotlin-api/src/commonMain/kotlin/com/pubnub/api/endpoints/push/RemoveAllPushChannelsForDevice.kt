package com.pubnub.api.endpoints.push

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult

/**
 * @see [PubNub.removeAllPushNotificationsFromDeviceWithPushToken]
 */
expect interface RemoveAllPushChannelsForDevice : Endpoint<PNPushRemoveAllChannelsResult>