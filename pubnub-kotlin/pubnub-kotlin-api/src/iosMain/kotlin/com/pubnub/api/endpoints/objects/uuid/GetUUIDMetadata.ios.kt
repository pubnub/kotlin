package com.pubnub.api.endpoints.objects.uuid

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.getUUIDMetadataWithUuid
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.createPNUUIDMetadata
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.getUUIDMetadata]
 */
actual interface GetUUIDMetadata : PNFuture<PNUUIDMetadataResult> {
}

@OptIn(ExperimentalForeignApi::class)
class GetUUIDMetadataImpl(
    private val pubnub: PubNubObjC,
    private val uuid: String?,
    private val includeCustom: Boolean
): GetUUIDMetadata {
    override fun async(callback: Consumer<Result<PNUUIDMetadataResult>>) {
        pubnub.getUUIDMetadataWithUuid(
            uuid = uuid,
            includeCustom = includeCustom,
            onSuccess = callback.onSuccessHandler {
                PNUUIDMetadataResult(
                    status = 200,
                    data = it?.let { createPNUUIDMetadata(from = it) }
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}