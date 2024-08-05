package com.pubnub.internal.models.consumer.objects.channel

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult
import com.pubnub.api.models.consumer.objects.channel.NewPNChannelMetadata

data class PNChannelMetadataArrayResult(
    val status: Int,
    val data: Collection<NewPNChannelMetadata>,
    val totalCount: Int?,
    val next: PNPage.PNNext?,
    val prev: PNPage.PNPrev?,
) {
    fun toApi(): PNChannelMetadataArrayResult {
        return PNChannelMetadataArrayResult(
            status,
            data,
            totalCount,
            next,
            prev,
        )
    }
}
