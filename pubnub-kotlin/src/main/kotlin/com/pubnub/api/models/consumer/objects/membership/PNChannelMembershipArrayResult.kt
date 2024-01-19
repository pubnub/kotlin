package com.pubnub.api.models.consumer.objects.membership

import com.pubnub.api.models.consumer.objects.PNPage

data class PNChannelMembershipArrayResult(
    val status: Int,
    val data: Collection<PNChannelMembership>,
    val totalCount: Int?,
    val next: PNPage.PNNext?,
    val prev: PNPage.PNPrev?
) {
    companion object {
        fun from(data: com.pubnub.internal.models.consumer.objects.membership.PNChannelMembershipArrayResult): PNChannelMembershipArrayResult {
            return PNChannelMembershipArrayResult(
                data.status,
                data.data.map(PNChannelMembership::from),
                data.totalCount,
                data.next,
                data.prev
            )
        }
    }
}
