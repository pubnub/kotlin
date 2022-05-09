package com.pubnub.entities.objects.user

data class PNUserArrayResult(
    val status: Int,
    val data: Collection<PNUser>,
    val totalCount: Int?,
    val next: com.pubnub.api.models.consumer.PNPage?,
    val prev: com.pubnub.api.models.consumer.PNPage?
)

data class PNUserResult(
    val status: Int,
    val data: PNUser?
)
