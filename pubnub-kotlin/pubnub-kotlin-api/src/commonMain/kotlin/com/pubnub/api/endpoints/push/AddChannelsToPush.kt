package com.pubnub.api.endpoints.push

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult

/**
 * @see [PubNub.addPushNotificationsOnChannels]
 */
expect interface AddChannelsToPush : PNFuture<PNPushAddChannelResult>