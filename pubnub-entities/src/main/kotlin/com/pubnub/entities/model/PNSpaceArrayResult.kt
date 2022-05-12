package com.pubnub.entities.model

import com.pubnub.api.models.consumer.objects.PNPage

class PNSpaceArrayResult(
    val status: Int,
    val data: Collection<PNSpace>,
    val totalCount: Int?,
    val next: PNPage.PNNext?,
    val prev: PNPage.PNPrev?
)