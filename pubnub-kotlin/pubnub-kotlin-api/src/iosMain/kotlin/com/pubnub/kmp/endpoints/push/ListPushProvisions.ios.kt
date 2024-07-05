package com.pubnub.kmp.endpoints.push

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.listPushChannelsWithDeviceId
import com.pubnub.kmp.PNFuture
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.endpoints.push.ListPushProvisions
import com.pubnub.kmp.toNSData
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData

@OptIn(ExperimentalForeignApi::class)
class ListPushProvisionsImpl(
    private val pubnub: PubNubObjC,
    private val deviceId: String,
    private val pushType: PNPushType,
    private val topic: String?,
    private val environment: PNPushEnvironment
): ListPushProvisions {
    override fun async(callback: Consumer<Result<PNPushListProvisionsResult>>) {
        deviceId.toNSData()?.let { data: NSData ->
            pubnub.listPushChannelsWithDeviceId(
                deviceId = data,
                pushType = pushType.toParamString(),
                topic = topic.orEmpty(),
                environment = environment.toParamString(),
                onSuccess = callback.onSuccessHandler { PNPushListProvisionsResult(channels = it?.filterIsInstance<String>() ?: emptyList()) },
                onFailure = callback.onFailureHandler()
            )
        } ?: callback.accept(Result.failure(PubNubException("Cannot create NSData from $deviceId")))
    }
}
