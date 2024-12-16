package com.pubnub.api.endpoints.objects.uuid

import cocoapods.PubNubSwift.KMPAnyJSON
import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.setUserMetadataWithMetadataId
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.CustomObject
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.createPNUUIDMetadata
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.setUUIDMetadata]
 */
actual interface SetUUIDMetadata : PNFuture<PNUUIDMetadataResult>

@OptIn(ExperimentalForeignApi::class)
class SetUUIDMetadataImpl(
    private val pubnub: KMPPubNub,
    private val metadataId: String?,
    private val name: String?,
    private val externalId: String?,
    private val profileUrl: String?,
    private val email: String?,
    private val custom: CustomObject?,
    private val includeCustom: Boolean,
    private val type: String?,
    private val status: String?
) : SetUUIDMetadata {
    override fun async(callback: Consumer<Result<PNUUIDMetadataResult>>) {
        pubnub.setUserMetadataWithMetadataId(
            metadataId = metadataId,
            name = name,
            externalId = externalId,
            profileUrl = profileUrl,
            email = email,
            custom = KMPAnyJSON(custom),
            includeCustom = includeCustom,
            type = type,
            status = status,
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
