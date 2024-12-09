package com.pubnub.api.endpoints.objects.channel

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.getChannelMetadataWithMetadataId
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.createPNChannelMetadata
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.getChannelMetadata]
 */
actual interface GetChannelMetadata : PNFuture<PNChannelMetadataResult>

@OptIn(ExperimentalForeignApi::class)
class GetChannelMetadataImpl(
    private val pubnub: KMPPubNub,
    private val metadataId: String,
    private val includeCustom: Boolean
) : GetChannelMetadata {
    override fun async(callback: Consumer<Result<PNChannelMetadataResult>>) {
        pubnub.getChannelMetadataWithMetadataId(
            metadataId = metadataId,
            includeCustom = includeCustom,
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
