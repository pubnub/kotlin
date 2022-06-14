package com.pubnub.api.models.consumer.objects.uuid

import com.pubnub.api.models.consumer.objects.PNPage

data class PNUUIDMetadataArrayResult(
    val status: Int,
    val data: Collection<PNUUIDMetadata>,
    val totalCount: Int?,
    val next: PNPage.PNNext?,
    val prev: PNPage.PNPrev?
)
