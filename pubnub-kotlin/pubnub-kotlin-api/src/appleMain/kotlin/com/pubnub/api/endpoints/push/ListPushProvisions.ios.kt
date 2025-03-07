package com.pubnub.api.endpoints.push

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.listPushChannelsWithDeviceId
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.auditPushChannelProvisions]
 */
actual interface ListPushProvisions : PNFuture<PNPushListProvisionsResult>

@OptIn(ExperimentalForeignApi::class)
class ListPushProvisionsImpl(
    private val pubnub: KMPPubNub,
    private val deviceId: String,
    private val pushType: PNPushType,
    private val topic: String?,
    private val environment: PNPushEnvironment
) : ListPushProvisions {
    override fun async(callback: Consumer<Result<PNPushListProvisionsResult>>) {
        pubnub.listPushChannelsWithDeviceId(
            deviceId = deviceId,
            pushType = pushType.toParamString(),
            topic = topic.orEmpty(),
            environment = environment.toParamString(),
            onSuccess = callback.onSuccessHandler {
                PNPushListProvisionsResult(
                    channels = it?.filterIsInstance<String>() ?: emptyList()
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}
