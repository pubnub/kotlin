package com.pubnub.api.endpoints.push

import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.removePushNotificationsFromChannels]
 */
actual interface RemoveChannelsFromPush : PNFuture<PNPushRemoveChannelResult>
