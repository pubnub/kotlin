package com.pubnub.internal.kotlin.endpoints.push

import com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDevice
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.push.IRemoveAllPushChannelsForDevice

/**
 * @see [PubNubImpl.removeAllPushNotificationsFromDeviceWithPushToken]
 */
class RemoveAllPushChannelsForDeviceImpl internal constructor(
    removeAllPushChannelsForDevice: IRemoveAllPushChannelsForDevice
) : IRemoveAllPushChannelsForDevice by removeAllPushChannelsForDevice, RemoveAllPushChannelsForDevice
