package com.pubnub.entities

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ComposableRemoteAction.Companion.firstDo
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction.Companion.map
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.entities.models.consumer.user.PNRemoveUserResult
import com.pubnub.entities.models.consumer.user.PNUserArrayResult
import com.pubnub.entities.models.consumer.user.PNUserResult
import com.pubnub.entities.models.consumer.user.toPNRemoveUserResult
import com.pubnub.entities.models.consumer.user.toPNUserArrayResult
import com.pubnub.entities.models.consumer.user.toPNUserResult

fun PubNub.fetchUsers(
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

fun PubNub.fetchUser(
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

fun PubNub.createUser(
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

fun PubNub.updateUser(
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

fun PubNub.removeUser(
    userId: String? = null
): ExtendedRemoteAction<PNRemoveUserResult?> = firstDo(
    removeUUIDMetadata(uuid = userId)
).then {
    map(
        it,
        PNOperationType.UserOperation
    ) { pnRemoveMetadataResult -> pnRemoveMetadataResult.toPNRemoveUserResult() }
}
