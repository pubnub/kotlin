package com.pubnub.api.endpoints.push

import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.push.IRemoveAllPushChannelsForDevice

/**
 * @see [PubNubImpl.removeAllPushNotificationsFromDeviceWithPushToken]
 */
class RemoveAllPushChannelsForDevice internal constructor(
    removeAllPushChannelsForDevice: IRemoveAllPushChannelsForDevice
) : IRemoveAllPushChannelsForDevice by removeAllPushChannelsForDevice
