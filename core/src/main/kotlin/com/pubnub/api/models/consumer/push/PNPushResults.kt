package com.pubnub.api.models.consumer.push

import com.pubnub.api.PubNub

/**
 * Result of [PubNub.addPushNotificationsOnChannels] operation.
 */
class PNPushAddChannelResult

class PNPushListProvisionsResult internal constructor(
    val channels: List<String>
)

class PNPushRemoveAllChannelsResult

class PNPushRemoveChannelResult
