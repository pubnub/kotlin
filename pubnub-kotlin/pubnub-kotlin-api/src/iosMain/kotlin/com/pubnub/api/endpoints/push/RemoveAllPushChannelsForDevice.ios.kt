package com.pubnub.api.endpoints.push

import cocoapods.PubNubSwift.PubNubObjC
import com.pubnub.api.Endpoint
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.toNSData
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData

/**
 * @see [PubNub.removeAllPushNotificationsFromDeviceWithPushToken]
 */
actual interface RemoveAllPushChannelsForDevice : Endpoint<PNPushRemoveAllChannelsResult>

@OptIn(ExperimentalForeignApi::class)
open class RemoveAllPushChannelsForDeviceImpl(
    private val pubnub: PubNubObjC,
    private val deviceId: String,
    private val pushType: PNPushType
): RemoveAllPushChannelsForDevice {
    override fun async(callback: Consumer<Result<PNPushRemoveAllChannelsResult>>) {
        deviceId.toNSData()?.let { data: NSData ->
            pubnub.removeAllChannelsFromPushWithPushType(
                pushType = pushType.toParamString(),
                deviceId = data,
                onSuccess = { callback.accept(Result.success(PNPushRemoveAllChannelsResult())) },
                onFailure = callback.onFailureHandler()
            )
        } ?: callback.accept(Result.failure(PubNubException("Cannot create NSData from $deviceId")))
    }
}