package com.pubnub.entities.models.consumer.user

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult

data class PNUserArrayResult(
    val status: Int,
    val data: Collection<PNUser>,
    val totalCount: Int?,
    val next: PNPage?,
    val prev: PNPage?
)

internal fun PNUUIDMetadataArrayResult.toPNUserArrayResult(): PNUserArrayResult? {
    if (this == null) {
        return null
    }
    val pnUserList = data.map { pnUserMetadata ->
        pnUserMetadata.toPNUser()
    }
    return PNUserArrayResult(status = status, data = pnUserList, totalCount = totalCount, next = next, prev = prev)
}
