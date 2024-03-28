package com.pubnub.internal.endpoints.push

import com.pubnub.api.endpoints.push.AddChannelsToPush
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.addPushNotificationsOnChannels]
 */
class AddChannelsToPushImpl internal constructor(addChannelsToPush: AddChannelsToPushInterface) :
    AddChannelsToPushInterface by addChannelsToPush, AddChannelsToPush
