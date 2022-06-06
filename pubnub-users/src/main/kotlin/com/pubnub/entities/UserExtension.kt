package com.pubnub.entities

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ComposableRemoteAction.Companion.firstDo
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction.Companion.map
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.ResultKey
import com.pubnub.api.models.consumer.objects.ResultSortKey
import com.pubnub.entities.models.consumer.user.RemoveUserResult
import com.pubnub.entities.models.consumer.user.UserResult
import com.pubnub.entities.models.consumer.user.UsersResult
import com.pubnub.entities.models.consumer.user.toRemoveUserResult
import com.pubnub.entities.models.consumer.user.toUserResult
import com.pubnub.entities.models.consumer.user.toUsersResult

/**
 * Returns a paginated list of User metadata, optionally including the custom data object for each.
 *
 * @param limit Number of objects to return in the response.
 *              Default is 100, which is also the maximum value.
 *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
 * @param page Use for pagination.
 *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
 *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
 *                           Ignored if you also supply the start parameter.
 * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
 *               expression are returned.
 * @param sort List of properties to sort by. Available options are id, name, and updated.
 *             e.g. listOf(ResultSortKey.Asc(key = ResultKey.ID)) , listOf(ResultSortKey.Desc(key = ResultKey.NAME))
 *             @see [Asc], [Desc]
 * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
 *                     Default is `false`.
 * @param includeCustom Include respective additional fields in the response.
 */
fun PubNub.fetchUsers(
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<ResultSortKey<ResultKey>> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false
): ExtendedRemoteAction<UsersResult?> = firstDo(
    getAllUUIDMetadata(
        limit = limit,
        page = page,
        filter = filter,
        sort = toPNSortKey(sort),
        includeCount = includeCount,
        includeCustom = includeCustom
    )
).then {
    map(
        it,
        PNOperationType.UserOperation
    ) { pnUuidMetadataArrayResult -> pnUuidMetadataArrayResult.toUsersResult() }
}

private fun toPNSortKey(sort: Collection<ResultSortKey<ResultKey>>): Collection<PNSortKey<PNKey>> {
    val pnSortKeyList = sort.map { resultSortKey ->
        when (resultSortKey) {
            is ResultSortKey.Asc -> PNSortKey.PNAsc(key = resultSortKey.key.toPNSortKey())
            is ResultSortKey.Desc -> PNSortKey.PNDesc(key = resultSortKey.key.toPNSortKey())
        }
    }
    return pnSortKeyList
}

/**
 * Returns metadata for the specified User, optionally including the custom data object for each.
 *
 * @param userId Unique user identifier. If not supplied then current user’s userId is used.
 * @param includeCustom Include respective additional fields in the response.
 */
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

/**
 * Create metadata for a User in the database, optionally including the custom data object for each.
 *
 * @param userId Unique user identifier. If not supplied then current user’s userId is used.
 * @param name Display name for the user. Maximum 200 characters.
 * @param externalId User's identifier in an external system
 * @param profileUrl The URL of the user's profile picture
 * @param email The user's email address. Maximum 80 characters.
 * @param custom Object with supported data types.
 * @param includeCustom Include respective additional fields in the response.
 */
fun PubNub.createUser(
    userId: String? = null,
    name: String? = null,
    externalId: String? = null,
    profileUrl: String? = null,
    email: String? = null,
    custom: Map<String, Any>? = null,
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

/**
 * Update existing metadata for a User in the database, optionally including the custom data object for each.
 *
 * @param userId Unique user identifier. If not supplied then current user’s userId is used.
 * @param name Display name for the user. Maximum 200 characters.
 * @param externalId User's identifier in an external system
 * @param profileUrl The URL of the user's profile picture
 * @param email The user's email address. Maximum 80 characters.
 * @param custom Object with supported data types.
 * @param includeCustom Include respective additional fields in the response.
 */
fun PubNub.updateUser(
    userId: String? = null,
    name: String? = null,
    externalId: String? = null,
    profileUrl: String? = null,
    email: String? = null,
    custom: Map<String, Any>? = null,
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

/**
 * Removes the metadata from a specified User.
 *
 * @param userId Unique user identifier. If not supplied then current user’s userId is used.
 */
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
