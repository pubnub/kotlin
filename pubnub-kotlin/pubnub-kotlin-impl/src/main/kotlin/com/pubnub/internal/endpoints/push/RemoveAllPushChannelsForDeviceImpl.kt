package com.pubnub.internal.endpoints.push

import com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDevice
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.removeAllPushNotificationsFromDeviceWithPushToken]
 */
class RemoveAllPushChannelsForDeviceImpl internal constructor(
    removeAllPushChannelsForDevice: RemoveAllPushChannelsForDeviceInterface,
) : RemoveAllPushChannelsForDeviceInterface by removeAllPushChannelsForDevice,
    RemoveAllPushChannelsForDevice,
    EndpointImpl<PNPushRemoveAllChannelsResult>(removeAllPushChannelsForDevice)
