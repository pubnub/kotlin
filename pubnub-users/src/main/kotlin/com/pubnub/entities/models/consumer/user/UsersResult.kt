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

internal fun PNUUIDMetadataArrayResult.toUsersResult(): UsersResult {
    val users = data.map { pnUserMetadata ->
        pnUserMetadata.toUser()
    }
    return UsersResult(status = status, data = users, totalCount = totalCount, next = next, prev = prev)
}
