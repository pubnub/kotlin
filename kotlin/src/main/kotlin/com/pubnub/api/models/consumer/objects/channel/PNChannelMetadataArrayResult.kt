package com.pubnub.api.models.consumer.objects.channel

import com.pubnub.api.models.consumer.objects.PNPage

data class PNChannelMetadataArrayResult(
    val status: Int,
    val data: Collection<PNChannelMetadata>,
    val totalCount: Int?,
    val next: PNPage.PNNext?,
    val prev: PNPage.PNPrev?
)
