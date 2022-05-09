package com.pubnub.entities.objects.space

import com.pubnub.entities.objects.PNPage

data class PNSpaceArrayResult(
    val status: Int,
    val data: Collection<PNSpace>,
    val totalCount: Int?,
    val next: PNPage.PNNext?,
    val prev: PNPage.PNPrev?
)

data class PNSpaceResult(
    val status: Int,
    val data: PNSpace?
)
