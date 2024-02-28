package com.pubnub.internal.endpoints.push

import com.pubnub.api.endpoints.push.RemoveChannelsFromPush
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.removePushNotificationsFromChannels]
 */
class RemoveChannelsFromPushImpl internal constructor(
    removeChannelsFromPush: RemoveChannelsFromPushInterface,
) : RemoveChannelsFromPushInterface by removeChannelsFromPush, RemoveChannelsFromPush
