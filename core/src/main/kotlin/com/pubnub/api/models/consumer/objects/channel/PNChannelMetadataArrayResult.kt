package com.pubnub.api.models.consumer.objects.channel

import com.pubnub.api.models.consumer.objects.PNPage

data class PNChannelMetadataArrayResult(
    val status: Int,
    val data: Collection<PNChannelMetadata>,
    val totalCount: Int?,
    val next: PNPage.PNNext?,
    val prev: PNPage.PNPrev?
) {
    companion object {
        fun from(data: com.pubnub.internal.models.consumer.objects.channel.PNChannelMetadataArrayResult): PNChannelMetadataArrayResult {
            return PNChannelMetadataArrayResult(
                data.status,
                data.data,
                data.totalCount,
                data.next,
                data.prev
            )
        }
    }
}
