package com.pubnub.api.models.consumer.objects.uuid

import com.pubnub.api.models.consumer.objects.PNPage

data class PNUUIDMetadataArrayResult(
    val status: Int,
    val data: Collection<PNUUIDMetadata>,
    val totalCount: Int?,
    val next: PNPage.PNNext?,
    val prev: PNPage.PNPrev?,
) {
    companion object {
        fun from(data: com.pubnub.internal.models.consumer.objects.uuid.PNUUIDMetadataArrayResult): PNUUIDMetadataArrayResult {
            return PNUUIDMetadataArrayResult(
                data.status,
                data.data,
                data.totalCount,
                data.next,
                data.prev,
            )
        }
    }
}
