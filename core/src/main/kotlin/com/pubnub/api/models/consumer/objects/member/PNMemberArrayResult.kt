package com.pubnub.api.models.consumer.objects.member

import com.pubnub.api.models.consumer.objects.PNPage

data class PNMemberArrayResult(
    val status: Int,
    val data: Collection<PNMember>,
    val totalCount: Int?,
    val next: PNPage.PNNext?,
    val prev: PNPage.PNPrev?
)
