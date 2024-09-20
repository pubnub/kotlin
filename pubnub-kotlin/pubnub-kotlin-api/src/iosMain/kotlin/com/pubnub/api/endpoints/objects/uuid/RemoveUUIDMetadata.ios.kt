package com.pubnub.api.endpoints.objects.uuid

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.removeUUIDMetadataWithUuid
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi

actual interface RemoveUUIDMetadata : PNFuture<PNRemoveMetadataResult>

@OptIn(ExperimentalForeignApi::class)
class RemoveUUIDMetadataImpl(
    private val pubnub: KMPPubNub,
    private val uuid: String?
) : RemoveUUIDMetadata {
    override fun async(callback: Consumer<Result<PNRemoveMetadataResult>>) {
        pubnub.removeUUIDMetadataWithUuid(
            uuid = uuid,
            onSuccess = callback.onSuccessHandler { PNRemoveMetadataResult(200) },
            onFailure = callback.onFailureHandler()
        )
    }
}
