package com.pubnub.kmp.endpoints.push

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult

/**
 * @see [PubNub.removePushNotificationsFromChannels]
 */
interface RemoveChannelsFromPush : PNFuture<PNPushRemoveChannelResult>