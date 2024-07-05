package com.pubnub.api.endpoints.push

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult

/**
 * @see [PubNub.addPushNotificationsOnChannels]
 */
interface AddChannelsToPush : com.pubnub.kmp.endpoints.push.AddChannelsToPush, Endpoint<PNPushAddChannelResult>
