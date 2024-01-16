package com.pubnub.api.endpoints.push

import com.pubnub.api.PubNub
import com.pubnub.internal.endpoints.push.IAddChannelsToPush

/**
 * @see [PubNub.addPushNotificationsOnChannels]
 */
class AddChannelsToPush internal constructor(addChannelsToPush: IAddChannelsToPush) :
    IAddChannelsToPush by addChannelsToPush
