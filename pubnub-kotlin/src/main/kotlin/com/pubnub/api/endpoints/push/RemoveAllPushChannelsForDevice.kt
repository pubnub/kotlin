package com.pubnub.api.endpoints.push

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.push.IRemoveAllPushChannelsForDevice
import com.pubnub.internal.endpoints.push.RemoveAllPushChannelsForDevice

/**
 * @see [PubNub.removeAllPushNotificationsFromDeviceWithPushToken]
 */
class RemoveAllPushChannelsForDevice internal constructor(
    private val removeAllPushChannelsForDevice: RemoveAllPushChannelsForDevice
) : DelegatingEndpoint<PNPushRemoveAllChannelsResult>(), IRemoveAllPushChannelsForDevice by removeAllPushChannelsForDevice {
    override fun createAction(): Endpoint<PNPushRemoveAllChannelsResult> = removeAllPushChannelsForDevice.mapIdentity()
}
