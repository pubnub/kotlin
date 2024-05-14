package com.pubnub.api.endpoints.push

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult

/**
 * @see [PubNub.removePushNotificationsFromChannels]
 */
expect interface RemoveChannelsFromPush : Endpoint<PNPushRemoveChannelResult>