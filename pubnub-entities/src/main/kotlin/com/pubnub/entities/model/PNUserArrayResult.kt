package com.pubnub.entities.model

import com.pubnub.api.models.consumer.objects.PNPage

data class PNUserArrayResult(
    val status: Int,
    val data: Collection<PNUser>,
    val totalCount: Int?,
    val next: PNPage.PNNext?,
    val prev: PNPage.PNPrev?
)
