package com.pubnub.api.endpoints.push

import com.pubnub.api.PubNub
import com.pubnub.internal.endpoints.push.IRemoveChannelsFromPush

/**
 * @see [PubNub.removePushNotificationsFromChannels]
 */
class RemoveChannelsFromPush internal constructor(
    removeChannelsFromPush: IRemoveChannelsFromPush
) : IRemoveChannelsFromPush by removeChannelsFromPush
