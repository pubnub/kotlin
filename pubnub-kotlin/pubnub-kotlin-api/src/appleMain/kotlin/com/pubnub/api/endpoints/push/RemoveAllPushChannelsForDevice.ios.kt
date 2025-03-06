package com.pubnub.api.endpoints.push

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.removeAllChannelsFromPushWithPushType
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessReturnValue
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.removeAllPushNotificationsFromDeviceWithPushToken]
 */
actual interface RemoveAllPushChannelsForDevice : PNFuture<PNPushRemoveAllChannelsResult>

@OptIn(ExperimentalForeignApi::class)
class RemoveAllPushChannelsForDeviceImpl(
    private val pubnub: KMPPubNub,
    private val deviceId: String,
    private val pushType: PNPushType,
    private val topic: String?,
    private val environment: PNPushEnvironment
) : RemoveAllPushChannelsForDevice {
    override fun async(callback: Consumer<Result<PNPushRemoveAllChannelsResult>>) {
        pubnub.removeAllChannelsFromPushWithPushType(
            pushType = pushType.toParamString(),
            deviceId = deviceId,
            topic = topic.orEmpty(),
            environment = environment.toParamString(),
            onSuccess = callback.onSuccessReturnValue(PNPushRemoveAllChannelsResult()),
            onFailure = callback.onFailureHandler()
        )
    }
}
