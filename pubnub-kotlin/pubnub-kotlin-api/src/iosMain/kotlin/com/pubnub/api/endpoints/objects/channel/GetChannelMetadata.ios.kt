package com.pubnub.api.endpoints.objects.channel

import cocoapods.PubNubSwift.PubNubGetChannelMetadataResultObjC
import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.getChannelMetadataWithChannel
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.getChannelMetadata]
 */
actual interface GetChannelMetadata : Endpoint<PNChannelMetadataResult>

@OptIn(ExperimentalForeignApi::class)
class GetChannelMetadataImpl(
    private val pubnub: PubNubObjC,
    private val channel: String,
    private val includeCustom: Boolean
): GetChannelMetadata {
    override fun async(callback: Consumer<Result<PNChannelMetadataResult>>) {
        pubnub.getChannelMetadataWithChannel(
            channel = channel,
            includeCustom = includeCustom,
            onSuccess = callback.onSuccessHandler {
                PNChannelMetadataResult(
                    status = 200, // TODO: Determine this field
                    data = PNChannelMetadata(
                        id = it?.id() ?: "",
                        name = it?.name(),
                        description = it?.descr(),
                        custom = it?.custom() as? Map<String, Any?>, // TODO: Check
                        updated = it?.updated(),
                        eTag = it?.eTag(),
                        type = it?.type(),
                        status = it?.status()
                    )
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}