package com.pubnub.api.endpoints.push

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult

/**
 * @see [PubNub.removeAllPushNotificationsFromDeviceWithPushToken]
 */
actual interface RemoveAllPushChannelsForDevice : PNFuture<PNPushRemoveAllChannelsResult>