package com.pubnub.api.endpoints.objects.uuid

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.getUUIDMetadataWithUuid
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.getUUIDMetadata]
 */
actual interface GetUUIDMetadata : Endpoint<PNUUIDMetadataResult> {
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
                    data = PNUUIDMetadata(
                        id = it?.id() ?: "",
                        name = it?.name(),
                        externalId = it?.externalId(),
                        profileUrl = it?.profileUrl(),
                        email = it?.email(),
                        custom = it?.custom() as? Map<String, Any?>, // TODO: Check
                        updated = it?.updated(),
                        eTag = it?.eTag(),
                        type = it?.type(),
                        status = it?.status()
                    )
                )
            },
            onFailure = callback.onFailureHandler())
    }
}