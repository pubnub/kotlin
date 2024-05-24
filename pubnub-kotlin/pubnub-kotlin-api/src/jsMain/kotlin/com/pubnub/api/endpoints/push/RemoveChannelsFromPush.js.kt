package com.pubnub.api.endpoints.push

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult

/**
 * @see [PubNub.removePushNotificationsFromChannels]
 */
actual interface RemoveChannelsFromPush : Endpoint<PNPushRemoveChannelResult>