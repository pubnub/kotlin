package com.pubnub.kmp.endpoints.push

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult

/**
 * @see [PubNub.addPushNotificationsOnChannels]
 */
interface AddChannelsToPush : PNFuture<PNPushAddChannelResult>