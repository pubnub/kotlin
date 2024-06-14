package com.pubnub.api.endpoints.push

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult

/**
 * @see [PubNub.removeAllPushNotificationsFromDeviceWithPushToken]
 */
expect interface RemoveAllPushChannelsForDevice : PNFuture<PNPushRemoveAllChannelsResult>