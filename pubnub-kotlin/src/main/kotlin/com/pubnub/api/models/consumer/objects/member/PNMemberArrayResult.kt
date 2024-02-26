package com.pubnub.api.models.consumer.objects.member

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.internal.models.consumer.objects.member.PNMemberArrayResult

data class PNMemberArrayResult(
    val status: Int,
    val data: Collection<PNMember>,
    val totalCount: Int?,
    val next: PNPage.PNNext?,
    val prev: PNPage.PNPrev?,
) {
    companion object {
        fun from(data: PNMemberArrayResult): com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult {
            return PNMemberArrayResult(
                data.status,
                data.data.map(PNMember::from),
                data.totalCount,
                data.next,
                data.prev,
            )
        }
    }
}
