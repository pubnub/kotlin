package com.pubnub.internal.models.consumer.objects.channel

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata

data class PNChannelMetadataArrayResult(
    val status: Int,
    val data: Collection<PNChannelMetadata>,
    val totalCount: Int?,
    val next: PNPage.PNNext?,
    val prev: PNPage.PNPrev?
)
