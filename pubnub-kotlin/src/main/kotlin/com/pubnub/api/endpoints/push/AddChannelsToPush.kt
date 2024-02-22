package com.pubnub.api.endpoints.push

import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.push.IAddChannelsToPush

/**
 * @see [PubNubImpl.addPushNotificationsOnChannels]
 */
class AddChannelsToPush internal constructor(addChannelsToPush: IAddChannelsToPush) :
    IAddChannelsToPush by addChannelsToPush
