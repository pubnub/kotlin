package com.pubnub.api.endpoints.push

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult

/**
 * @see [PubNub.addPushNotificationsOnChannels]
 */
actual interface AddChannelsToPush : Endpoint<PNPushAddChannelResult>