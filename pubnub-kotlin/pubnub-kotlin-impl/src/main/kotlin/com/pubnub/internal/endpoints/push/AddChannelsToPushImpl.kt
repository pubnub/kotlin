package com.pubnub.internal.endpoints.push

import com.pubnub.api.endpoints.push.AddChannelsToPush
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.addPushNotificationsOnChannels]
 */
class AddChannelsToPushImpl internal constructor(addChannelsToPush: AddChannelsToPushInterface) :
    AddChannelsToPushInterface by addChannelsToPush,
    AddChannelsToPush,
    EndpointImpl<PNPushAddChannelResult>(addChannelsToPush)
