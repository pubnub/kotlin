package com.pubnub.kmp.endpoints.push

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult

/**
 * @see [PubNub.removeAllPushNotificationsFromDeviceWithPushToken]
 */
interface RemoveAllPushChannelsForDevice : PNFuture<PNPushRemoveAllChannelsResult>