package com.pubnub.api.endpoints.push

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.listPushChannelsWithDeviceId
import com.pubnub.api.Endpoint
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.toNSData
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData

/**
 * @see [PubNub.auditPushChannelProvisions]
 */
actual interface ListPushProvisions : Endpoint<PNPushListProvisionsResult>

@OptIn(ExperimentalForeignApi::class)
class ListPushProvisionsImpl(
    private val pubnub: PubNubObjC,
    private val deviceId: String,
    private val pushType: PNPushType
): ListPushProvisions {
    override fun async(callback: Consumer<Result<PNPushListProvisionsResult>>) {
        deviceId.toNSData()?.let { data: NSData ->
            pubnub.listPushChannelsWithDeviceId(
                deviceId = data,
                pushType = pushType.toParamString(),
                onSuccess = callback.onSuccessHandler { PNPushListProvisionsResult(channels = it as List<String>) },
                onFailure = callback.onFailureHandler()
            )
        } ?: callback.accept(Result.failure(PubNubException("Cannot create NSData from $deviceId")))
    }
}
