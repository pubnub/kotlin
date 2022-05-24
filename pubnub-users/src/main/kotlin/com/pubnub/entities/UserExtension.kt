package com.pubnub.entities

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ComposableRemoteAction.Companion.firstDo
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction.Companion.map
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.entities.models.consumer.user.RemoveUserResult
import com.pubnub.entities.models.consumer.user.UserResult
import com.pubnub.entities.models.consumer.user.UsersResult
import com.pubnub.entities.models.consumer.user.toRemoveUserResult
import com.pubnub.entities.models.consumer.user.toUserResult
import com.pubnub.entities.models.consumer.user.toUsersResult

fun PubNub.fetchUsers(
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false
): ExtendedRemoteAction<UsersResult?> = firstDo(
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
    ) { pnUuidMetadataArrayResult -> pnUuidMetadataArrayResult.toUsersResult() }
}

fun PubNub.fetchUser(
    userId: String? = configuration.uuid,
    includeCustom: Boolean = false
): ExtendedRemoteAction<UserResult?> = firstDo(
    getUUIDMetadata(
        uuid = userId,
        includeCustom = includeCustom
    )
).then {
    map(
        it,
        PNOperationType.UserOperation
    ) { pnUuidMetadataResult -> pnUuidMetadataResult.toUserResult() }
}

fun PubNub.createUser(
    userId: String? = null,
    name: String? = null,
    externalId: String? = null,
    profileUrl: String? = null,
    email: String? = null,
    custom: Any? = null,
    includeCustom: Boolean = false
): ExtendedRemoteAction<UserResult?> = firstDo(
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
    ) { pnUuidMetadataResult -> pnUuidMetadataResult.toUserResult() }
}

fun PubNub.updateUser(
    userId: String? = null,
    name: String? = null,
    externalId: String? = null,
    profileUrl: String? = null,
    email: String? = null,
    custom: Any? = null,
    includeCustom: Boolean = false
): ExtendedRemoteAction<UserResult?> = firstDo(
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
    ) { pnUuidMetadataResult -> pnUuidMetadataResult.toUserResult() }
}

fun PubNub.removeUser(
    userId: String? = null
): ExtendedRemoteAction<RemoveUserResult?> = firstDo(
    removeUUIDMetadata(uuid = userId)
).then {
    map(
        it,
        PNOperationType.UserOperation
    ) { pnRemoveMetadataResult -> pnRemoveMetadataResult.toRemoveUserResult() }
}
