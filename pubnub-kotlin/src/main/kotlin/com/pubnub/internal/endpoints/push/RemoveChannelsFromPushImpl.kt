package com.pubnub.internal.endpoints.push

import com.pubnub.api.endpoints.push.RemoveChannelsFromPush
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.removePushNotificationsFromChannels]
 */
class RemoveChannelsFromPushImpl internal constructor(
    removeChannelsFromPush: IRemoveChannelsFromPush,
) : IRemoveChannelsFromPush by removeChannelsFromPush, RemoveChannelsFromPush
