package com.pubnub.api.models.consumer.objects.uuid

import com.pubnub.internal.models.consumer.objects.uuid.PNUUIDMetadataResult

data class PNUUIDMetadataResult(
    val status: Int,
    val data: PNUUIDMetadata?
) {
    companion object {
        fun from(data: PNUUIDMetadataResult): com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult {
            return com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult(
                data.status,
                data.data
            )
        }
    }
}

