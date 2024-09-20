package com.pubnub.api.endpoints.push

import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.removeAllPushNotificationsFromDeviceWithPushToken]
 */
expect interface RemoveAllPushChannelsForDevice : PNFuture<PNPushRemoveAllChannelsResult>
