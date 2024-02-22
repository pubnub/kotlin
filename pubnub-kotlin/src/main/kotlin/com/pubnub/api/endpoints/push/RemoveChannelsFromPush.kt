package com.pubnub.api.endpoints.push

import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.push.IRemoveChannelsFromPush

/**
 * @see [PubNubImpl.removePushNotificationsFromChannels]
 */
class RemoveChannelsFromPush internal constructor(
    removeChannelsFromPush: IRemoveChannelsFromPush
) : IRemoveChannelsFromPush by removeChannelsFromPush
