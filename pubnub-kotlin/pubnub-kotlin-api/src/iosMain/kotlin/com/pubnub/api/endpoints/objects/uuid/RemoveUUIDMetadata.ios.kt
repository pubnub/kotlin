package com.pubnub.api.endpoints.objects.uuid

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.removeUUIDMetadataWithUuid
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi

actual interface RemoveUUIDMetadata : Endpoint<PNRemoveMetadataResult> {
}

@OptIn(ExperimentalForeignApi::class)
class RemoveUUIDMetadataImpl(
    private val pubnub: PubNubObjC,
    private val uuid: String?
): RemoveUUIDMetadata {
    override fun async(callback: Consumer<Result<PNRemoveMetadataResult>>) {
        pubnub.removeUUIDMetadataWithUuid(
            uuid = uuid,
            onSuccess = callback.onSuccessHandler { PNRemoveMetadataResult(200) },
            onFailure = callback.onFailureHandler()
        )
    }
}