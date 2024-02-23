package com.pubnub.internal.kotlin.endpoints.push

import com.pubnub.api.endpoints.push.AddChannelsToPush
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.push.IAddChannelsToPush

/**
 * @see [PubNubImpl.addPushNotificationsOnChannels]
 */
class AddChannelsToPushImpl internal constructor(addChannelsToPush: IAddChannelsToPush) :
    IAddChannelsToPush by addChannelsToPush, AddChannelsToPush
