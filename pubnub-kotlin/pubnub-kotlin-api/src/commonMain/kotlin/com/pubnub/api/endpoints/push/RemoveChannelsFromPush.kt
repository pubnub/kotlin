package com.pubnub.api.endpoints.push

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult

/**
 * @see [PubNub.removePushNotificationsFromChannels]
 */
expect interface RemoveChannelsFromPush : PNFuture<PNPushRemoveChannelResult>