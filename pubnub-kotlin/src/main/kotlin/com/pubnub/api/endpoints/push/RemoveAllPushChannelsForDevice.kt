package com.pubnub.api.endpoints.push

import com.pubnub.api.PubNub
import com.pubnub.internal.endpoints.push.IRemoveAllPushChannelsForDevice

/**
 * @see [PubNub.removeAllPushNotificationsFromDeviceWithPushToken]
 */
class RemoveAllPushChannelsForDevice internal constructor(
    removeAllPushChannelsForDevice: IRemoveAllPushChannelsForDevice
) : IRemoveAllPushChannelsForDevice by removeAllPushChannelsForDevice
