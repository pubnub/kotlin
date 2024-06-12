package com.pubnub.api.endpoints.objects.channel

import cocoapods.PubNubSwift.AnyJSONObjC
import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.setChannelMetadataWithChannel
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.CustomObject
import kotlinx.cinterop.ExperimentalForeignApi
import platform.posix.stat

/**
 * @see [PubNub.setChannelMetadata]
 */
actual interface SetChannelMetadata : Endpoint<PNChannelMetadataResult>

@OptIn(ExperimentalForeignApi::class)
class SetChannelMetadataImpl(
    private val pubnub: PubNubObjC,
    private val channel: String,
    private val name: String?,
    private val description: String?,
    private val custom: CustomObject?,
    private val includeCustom: Boolean,
    private val type: String?,
    private val status: String?
): SetChannelMetadata {
    override fun async(callback: Consumer<Result<PNChannelMetadataResult>>) {
        pubnub.setChannelMetadataWithChannel(
            channel = channel,
            name = name,
            description = description,
            custom = AnyJSONObjC(value = custom?.value),
            includeCustom = includeCustom,
            type = type,
            status = status,
            onSuccess = callback.onSuccessHandler {
                PNChannelMetadataResult(
                    status = 200,
                    data = PNChannelMetadata(
                        id = it?.id() ?: "",
                        name = it?.name(),
                        description = it?.descr(),
                        custom = it?.custom()?.asMap() as? Map<String, Any>, // TODO: Verify
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