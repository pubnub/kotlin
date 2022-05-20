package com.pubnub.entities.models.consumer.user

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult

data class UsersResult(
    val status: Int,
    val data: Collection<User>,
    val totalCount: Int?,
    val next: PNPage?,
    val prev: PNPage?
)

internal fun PNUUIDMetadataArrayResult.toPNUserArrayResult(): UsersResult? {
    if (this == null) {
        return null
    }
    val pnUserList = data.map { pnUserMetadata ->
        pnUserMetadata.toPNUser()
    }
    return UsersResult(status = status, data = pnUserList, totalCount = totalCount, next = next, prev = prev)
}
