package com.pubnub.api.endpoints.push

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult

/**
 * @see [PubNub.addPushNotificationsOnChannels]
 */
actual interface AddChannelsToPush : PNFuture<PNPushAddChannelResult>