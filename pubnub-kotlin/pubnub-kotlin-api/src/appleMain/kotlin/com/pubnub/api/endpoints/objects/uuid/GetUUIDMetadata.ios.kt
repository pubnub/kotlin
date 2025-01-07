package com.pubnub.api.endpoints.objects.uuid

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.KMPUserIncludeFields
import cocoapods.PubNubSwift.getUserMetadataWithMetadataId
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.createPNUUIDMetadata
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.getUUIDMetadata]
 */
actual interface GetUUIDMetadata : PNFuture<PNUUIDMetadataResult>

@OptIn(ExperimentalForeignApi::class)
class GetUUIDMetadataImpl(
    private val pubnub: KMPPubNub,
    private val metadataId: String?,
    private val includeCustom: Boolean
) : GetUUIDMetadata {
    override fun async(callback: Consumer<Result<PNUUIDMetadataResult>>) {
        pubnub.getUserMetadataWithMetadataId(
            metadataId = metadataId,
            include = KMPUserIncludeFields(
                includeCustom = includeCustom,
                includeType = true,
                includeStatus = true
            ),
            onSuccess = callback.onSuccessHandler {
                PNUUIDMetadataResult(
                    status = 200,
                    data = createPNUUIDMetadata(it)
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}
