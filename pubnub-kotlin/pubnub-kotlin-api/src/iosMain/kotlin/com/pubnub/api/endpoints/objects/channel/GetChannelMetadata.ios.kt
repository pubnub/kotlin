package com.pubnub.api.endpoints.objects.channel

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.getChannelMetadataWithChannel
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
    private val pubnub: PubNubObjC,
    private val channel: String,
    private val includeCustom: Boolean
) : GetChannelMetadata {
    override fun async(callback: Consumer<Result<PNChannelMetadataResult>>) {
        pubnub.getChannelMetadataWithChannel(
            channel = channel,
            includeCustom = includeCustom,
            onSuccess = callback.onSuccessHandler {
                PNChannelMetadataResult(
                    status = 200,
                    data = it?.let { rawValue -> createPNChannelMetadata(from = rawValue) }
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}
