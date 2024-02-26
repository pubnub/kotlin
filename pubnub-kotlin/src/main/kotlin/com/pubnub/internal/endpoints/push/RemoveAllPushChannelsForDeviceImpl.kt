package com.pubnub.internal.endpoints.push

import com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDevice
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.removeAllPushNotificationsFromDeviceWithPushToken]
 */
class RemoveAllPushChannelsForDeviceImpl internal constructor(
    removeAllPushChannelsForDevice: IRemoveAllPushChannelsForDevice,
) : IRemoveAllPushChannelsForDevice by removeAllPushChannelsForDevice, RemoveAllPushChannelsForDevice
