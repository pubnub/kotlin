package com.pubnub.api.endpoints.objects.uuid

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.setUUIDMetadataWithUuid
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.CustomObject
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.setUUIDMetadata]
 */
actual interface SetUUIDMetadata : Endpoint<PNUUIDMetadataResult>

@OptIn(ExperimentalForeignApi::class)
class SetUUIDMetadataImpl(
    private val pubnub: PubNubObjC,
    private val uuid: String?,
    private val name: String?,
    private val externalId: String?,
    private val profileUrl: String?,
    private val email: String?,
    private val custom: CustomObject?,
    private val includeCustom: Boolean,
    private val type: String?,
    private val status: String?
): SetUUIDMetadata {
    override fun async(callback: Consumer<Result<PNUUIDMetadataResult>>) {
        pubnub.setUUIDMetadataWithUuid(
            uuid = uuid,
            name = name,
            externalId = externalId,
            profileUrl = profileUrl,
            email = email,
            custom = null, // TODO: How to retrieve value from CustomObject?
            includeCustom = includeCustom,
            type = type,
            status = status,
            onSuccess = callback.onSuccessHandler {
                PNUUIDMetadataResult(
                    status = 200,
                    data = PNUUIDMetadata(
                        id = it?.id() ?: "",
                        name = it?.name(),
                        externalId = it?.externalId(),
                        profileUrl = it?.profileUrl(),
                        email = it?.email(),
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