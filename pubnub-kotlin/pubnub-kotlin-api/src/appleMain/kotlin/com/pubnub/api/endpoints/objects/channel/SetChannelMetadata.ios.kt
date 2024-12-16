package com.pubnub.api.endpoints.objects.channel

import cocoapods.PubNubSwift.KMPAnyJSON
import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.setChannelMetadataWithMetadataId
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.CustomObject
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.createPNChannelMetadata
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.setChannelMetadata]
 */
actual interface SetChannelMetadata : PNFuture<PNChannelMetadataResult>

@OptIn(ExperimentalForeignApi::class)
class SetChannelMetadataImpl(
    private val pubnub: KMPPubNub,
    private val metadataId: String,
    private val name: String?,
    private val description: String?,
    private val custom: CustomObject?,
    private val includeCustom: Boolean,
    private val type: String?,
    private val status: String?
) : SetChannelMetadata {
    override fun async(callback: Consumer<Result<PNChannelMetadataResult>>) {
        pubnub.setChannelMetadataWithMetadataId(
            metadataId = metadataId,
            name = name,
            description = description,
            custom = KMPAnyJSON(custom),
            includeCustom = includeCustom,
            type = type,
            status = status,
            onSuccess = callback.onSuccessHandler {
                PNChannelMetadataResult(
                    status = 200,
                    data = createPNChannelMetadata(it)
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}
