package com.pubnub.entities

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ComposableRemoteAction.Companion.firstDo
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction.Companion.map
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.entities.model.PNRemoveUserResult
import com.pubnub.entities.model.PNUser
import com.pubnub.entities.model.PNUserArrayResult
import com.pubnub.entities.model.PNUserResult

fun PubNub.getUsers(
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false
): ExtendedRemoteAction<PNUserArrayResult?> = firstDo(
    getAllUUIDMetadata(
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        includeCount = includeCount,
        includeCustom = includeCustom
    )
).then {
    map(
        it,
        PNOperationType.UserOperation
    ) { pnUuidMetadataArrayResult -> pnUuidMetadataArrayResult.toPNUserArrayResult() }

}

fun PubNub.getUser(
    userId: String? = null,
    includeCustom: Boolean = false
): ExtendedRemoteAction<PNUserResult?> = firstDo(
    getUUIDMetadata(
        uuid = userId ?: configuration.uuid,
        includeCustom = includeCustom
    )
).then {
    map(
        it,
        PNOperationType.UserOperation
    ) { pnUuidMetadataResult -> pnUuidMetadataResult.toPNUserResult() }
}

fun PubNub.setUser(
    userId: String? = null,
    name: String? = null,
    externalId: String? = null,
    profileUrl: String? = null,
    email: String? = null,
    custom: Any? = null,
    includeCustom: Boolean = false
): ExtendedRemoteAction<PNUserResult?> = firstDo(
    setUUIDMetadata(
    uuid = userId,
    name = name,
    externalId = externalId,
    profileUrl = profileUrl,
    email = email,
    custom = custom,
    includeCustom = includeCustom
)
).then {
    map(
        it,
        PNOperationType.UserOperation
    ) { pnUuidMetadataResult -> pnUuidMetadataResult.toPNUserResult() }
}

fun PubNub.deleteUser(
    userId: String? = null
): ExtendedRemoteAction<PNRemoveUserResult?> = firstDo(
    removeUUIDMetadata(uuid = userId)
).then {
    map(
        it,
        PNOperationType.UserOperation
    ){ pnRemoveMetadataResult ->  pnRemoveMetadataResult.toPNRemoveUserResult()}
}

internal fun PNUUIDMetadataArrayResult.toPNUserArrayResult(): PNUserArrayResult? {
    if (this == null) {
        return null
    }
    val pnUserList = data.map { pnUserMetadata ->
        pnUserMetadata.toPNUser()
    }
    return PNUserArrayResult(status = status, data = pnUserList, totalCount = totalCount, next = next, prev = prev)
}

internal fun PNUUIDMetadataResult.toPNUserResult() : PNUserResult? {
    if(this == null){
        return null
    }
    return PNUserResult(status = status, data = data?.toPNUser())
}

internal fun PNUUIDMetadata.toPNUser(): PNUser {
    return PNUser(
        id = id,
        name = name,
        externalId = externalId,
        profileUrl = profileUrl,
        email = email,
        custom = custom,
        updated = updated,
        eTag = eTag
    )
}

internal fun PNRemoveMetadataResult.toPNRemoveUserResult() : PNRemoveUserResult {
    return this?.let { PNRemoveUserResult(status = it.status) }
}