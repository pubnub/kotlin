package com.pubnub.internal.kotlin.endpoints.push

import com.pubnub.api.endpoints.push.RemoveChannelsFromPush
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.push.IRemoveChannelsFromPush

/**
 * @see [PubNubImpl.removePushNotificationsFromChannels]
 */
class RemoveChannelsFromPushImpl internal constructor(
    removeChannelsFromPush: IRemoveChannelsFromPush
) : IRemoveChannelsFromPush by removeChannelsFromPush, RemoveChannelsFromPush
