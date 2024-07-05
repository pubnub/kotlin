package com.pubnub.kmp.endpoints.push

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.removeAllChannelsFromPushWithPushType
import com.pubnub.kmp.PNFuture
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessReturnValue
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.endpoints.push.RemoveAllPushChannelsForDevice
import com.pubnub.kmp.toNSData
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData

@OptIn(ExperimentalForeignApi::class)
class RemoveAllPushChannelsForDeviceImpl(
    private val pubnub: PubNubObjC,
    private val deviceId: String,
    private val pushType: PNPushType,
    private val topic: String?,
    private val environment: PNPushEnvironment
): RemoveAllPushChannelsForDevice {
    override fun async(callback: Consumer<Result<PNPushRemoveAllChannelsResult>>) {
        deviceId.toNSData()?.let { data: NSData ->
            pubnub.removeAllChannelsFromPushWithPushType(
                pushType = pushType.toParamString(),
                deviceId = data,
                topic = topic.orEmpty(),
                environment = environment.toParamString(),
                onSuccess = callback.onSuccessReturnValue(PNPushRemoveAllChannelsResult()),
                onFailure = callback.onFailureHandler()
            )
        } ?: callback.accept(Result.failure(PubNubException("Cannot create NSData from $deviceId")))
    }
}