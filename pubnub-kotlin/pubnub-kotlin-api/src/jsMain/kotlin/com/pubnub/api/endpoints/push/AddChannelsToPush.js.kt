package com.pubnub.api.endpoints.push

import com.pubnub.api.models.consumer.push.PNPushAddChannelResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.addPushNotificationsOnChannels]
 */
actual interface AddChannelsToPush : PNFuture<PNPushAddChannelResult>
