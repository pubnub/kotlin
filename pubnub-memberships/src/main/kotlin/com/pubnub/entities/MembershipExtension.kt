package com.pubnub.entities

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ComposableRemoteAction.Companion.firstDo
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction.Companion.map
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNMemberKey
import com.pubnub.api.models.consumer.objects.PNMembershipKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.ResultSortKey
import com.pubnub.entities.models.consumer.membership.MembershipsResult
import com.pubnub.entities.models.consumer.membership.MembershipsStatusResult
import com.pubnub.entities.models.consumer.membership.SpaceDetailsLevel
import com.pubnub.entities.models.consumer.membership.SpaceIdWithCustom
import com.pubnub.entities.models.consumer.membership.SpaceMembershipResultKey
import com.pubnub.entities.models.consumer.membership.UserDetailsLevel
import com.pubnub.entities.models.consumer.membership.UserIdWithCustom
import com.pubnub.entities.models.consumer.membership.UserMembershipsResultKey
import com.pubnub.entities.models.consumer.membership.toPNChannelDetailsLevel
import com.pubnub.entities.models.consumer.membership.toPNChannelWithCustomList
import com.pubnub.entities.models.consumer.membership.toPNUUIDDetailsLevel
import com.pubnub.entities.models.consumer.membership.toPNUUIDWithCustomList
import com.pubnub.entities.models.consumer.membership.toSpaceFetchMembershipResult
import com.pubnub.entities.models.consumer.membership.toSpaceMembershipResult
import com.pubnub.entities.models.consumer.membership.toUserFetchMembershipsResult
import com.pubnub.entities.models.consumer.membership.toUserMembershipsResult

/**
 * Add memberships of user i.e. assign spaces to user, add user to spaces
 *
 * @param spaceIdsWithCustoms List of spaces to add the user to. List can contain only space ids or ids along with custom data
 *                            @see [SpaceIdWithCustom]
 * @param userId Unique user identifier. If not supplied then current user’s userId is used.
 *
 */
fun PubNub.addMembershipsOfUser(
    spaceIdsWithCustoms: List<SpaceIdWithCustom>,
    userId: String = configuration.uuid
): ExtendedRemoteAction<MembershipsStatusResult> = firstDo(
    setMemberships(
        channels = spaceIdsWithCustoms.toPNChannelWithCustomList(),
        uuid = userId,
        limit = 0
    )
).then {
    map(
        it,
        PNOperationType.MembershipOperation
    ) { pnChannelMembershipArrayResult -> pnChannelMembershipArrayResult.toUserMembershipsResult() }
}

/**
 * Add memberships of space i.e. add a users to a space, make users members of space
 *
 * @param spaceId Unique space identifier.
 * @param userIdsWithCustoms List of users to add to the space. List can contain only user ids or ids along with custom data
 *                             @see [UserIdWithCustom]
 */
fun PubNub.addMembershipsOfSpace(
    spaceId: String,
    userIdsWithCustoms: List<UserIdWithCustom>
): ExtendedRemoteAction<MembershipsStatusResult> = firstDo(
    setChannelMembers(
        channel = spaceId,
        uuids = userIdsWithCustoms.toPNUUIDWithCustomList(),
        limit = 0
    )
).then {
    map(
        it,
        PNOperationType.MembershipOperation
    ) { pnMemberArrayResult -> pnMemberArrayResult.toSpaceMembershipResult() }
}

/**
 * The method returns a list of space memberships for a user. This method doesn't return a user's subscriptions.
 *
 * @param userId Unique user identifier. If not supplied then current user’s userId is used.
 * @param limit Number of objects to return in the response.
 *              Default is 100, which is also the maximum value.
 *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
 * @param page Use for pagination.
 *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
 *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
 *                           Ignored if you also supply the start parameter.
 * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
 *               expression are returned.
 * @param sort List of properties to sort by. Available options are space.id, space.name, space.updated and updated.
 *             e.g. listOf(ResultSortKey.Desc(key = UserMembershipsResultKey.SPACE_NAME))
 *             @see [ResultSortKey.Asc], [ResultSortKey.Desc]
 * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
 *                     Default is `false`.
 * @param includeCustom Include respective additional fields in the response.
 * @param includeSpaceDetails Include custom fields for spaces metadata.
 */
fun PubNub.fetchMembershipsOfUser(
    userId: String = configuration.uuid,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<ResultSortKey<UserMembershipsResultKey>> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeSpaceDetails: SpaceDetailsLevel? = null
): ExtendedRemoteAction<MembershipsResult?> = firstDo(
    getMemberships(
        uuid = userId,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort.toPNMembershipSortKeyList(),
        includeCount = includeCount,
        includeCustom = includeCustom,
        includeChannelDetails = includeSpaceDetails?.toPNChannelDetailsLevel()
    )
).then {
    map(
        it,
        PNOperationType.MembershipOperation
    ) { pnChannelMembershipArrayResult -> pnChannelMembershipArrayResult.toUserFetchMembershipsResult(userId) }
}

private fun Collection<ResultSortKey<UserMembershipsResultKey>>.toPNMembershipSortKeyList(): Collection<PNSortKey<PNMembershipKey>> {
    return this.map { resultSortKey ->
        resultSortKey.toPNMembershipSortKey()
    }
}

private fun ResultSortKey<UserMembershipsResultKey>.toPNMembershipSortKey(): PNSortKey<PNMembershipKey> {
    return when (this) {
        is ResultSortKey.Asc -> PNSortKey.PNAsc(key = this.key.toPNMembershipKey())
        is ResultSortKey.Desc -> PNSortKey.PNDesc(key = this.key.toPNMembershipKey())
    }
}

/**
 * The method returns a list of users in a space. The list will include metadata for users
 * that have additional metadata stored in the database.
 *
 * @param spaceId Unique space identifier.
 * @param limit Number of objects to return in the response.
 *              Default is 100, which is also the maximum value.
 *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
 * @param page Use for pagination.
 *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
 *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
 *                           Ignored if you also supply the start parameter.
 * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
 *               expression are returned.
 * @param sort List of properties to sort by. Available options are user.id, user.name, user.updated and updated.
 *             e.g. listOf(ResultSortKey.Desc(key = SpaceMembershipResultKey.USER_ID))
 *             @see [PNAsc], [PNDesc]
 * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
 *                     Default is `false`.
 * @param includeCustom Include respective additional fields in the response.
 * @param includeUserDetails Include custom fields for users metadata.
 */
fun PubNub.fetchMembershipsOfSpace(
    spaceId: String,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<ResultSortKey<SpaceMembershipResultKey>> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeUserDetails: UserDetailsLevel? = null
): ExtendedRemoteAction<MembershipsResult?> = firstDo(
    getChannelMembers(
        channel = spaceId,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort.toPNMemberSortKeyList(),
        includeCount = includeCount,
        includeCustom = includeCustom,
        includeUUIDDetails = includeUserDetails?.toPNUUIDDetailsLevel()
    )
).then {
    map(
        it,
        PNOperationType.MembershipOperation
    ) { pnMemberArrayResult -> pnMemberArrayResult.toSpaceFetchMembershipResult(spaceId) }
}

private fun Collection<ResultSortKey<SpaceMembershipResultKey>>.toPNMemberSortKeyList(): Collection<PNSortKey<PNMemberKey>> {
    return this.map { resultSortKey ->
        resultSortKey.toPNMemberSortKey()
    }
}

private fun ResultSortKey<SpaceMembershipResultKey>.toPNMemberSortKey(): PNSortKey<PNMemberKey> {
    return when (this) {
        is ResultSortKey.Asc -> PNSortKey.PNAsc(key = this.key.toPNMemberKey())
        is ResultSortKey.Desc -> PNSortKey.PNDesc(key = this.key.toPNMemberKey())
    }
}

/**
 * Remove memberships of user
 *
 * @param spaceIds List of space ids to remove the user from.
 * @param userId Unique user identifier. If not supplied then current user’s userId is used.
 *
 */
fun PubNub.removeMembershipsOfUser(
    spaceIds: List<String>,
    userId: String = configuration.uuid,
): ExtendedRemoteAction<MembershipsStatusResult> = firstDo(
    removeMemberships(
        channels = spaceIds,
        uuid = userId,
        limit = 0
    )
).then {
    map(
        it,
        PNOperationType.MembershipOperation
    ) { pnChannelMembershipArrayResult -> pnChannelMembershipArrayResult.toUserMembershipsResult() }
}

/**
 * Remove memberships of space
 *
 * @param spaceId Unique space identifier.
 * @param userIds List of users to remove from the channel.
 */
fun PubNub.removeMembershipsOfSpace(
    spaceId: String,
    userIds: List<String>,
): ExtendedRemoteAction<MembershipsStatusResult> = firstDo(
    removeChannelMembers(
        channel = spaceId,
        uuids = userIds,
        limit = 0
    )
).then {
    map(
        it,
        PNOperationType.MembershipOperation
    ) { pnMemberArrayResult -> pnMemberArrayResult.toSpaceMembershipResult() }
}

/**
 * Update memberships of user. Using this method you can add the user to spaces and/or update membership custom data.
 *
 * @param spaceIdsWithCustoms List of spaces to add the user to. List can contain only space ids or ids along with custom data
 *                            @see [SpaceIdWithCustom]
 * @param userId Unique user identifier. If not supplied then current user’s userId is used.
 *
 */
fun PubNub.updateMembershipsOfUser(
    spaceIdsWithCustoms: List<SpaceIdWithCustom>,
    userId: String = configuration.uuid
): ExtendedRemoteAction<MembershipsStatusResult> = firstDo(
    setMemberships(
        channels = spaceIdsWithCustoms.toPNChannelWithCustomList(),
        uuid = userId,
        limit = 0
    )
).then {
    map(
        it,
        PNOperationType.MembershipOperation
    ) { pnChannelMembershipArrayResult -> pnChannelMembershipArrayResult.toUserMembershipsResult() }
}

/**
 * Update memberships of space. Using this method you can add users to the space and/or update membership custom data.
 *
 * @param spaceId Unique space identifier.
 * @param userIdsWithCustoms List of users to add to the space. List can contain only user ids or ids along with custom data
 *                             @see [UserIdWithCustom]
 */
fun PubNub.updateMembershipsOfSpace(
    spaceId: String,
    userIdsWithCustoms: List<UserIdWithCustom>
): ExtendedRemoteAction<MembershipsStatusResult> = firstDo(
    setChannelMembers(
        channel = spaceId,
        uuids = userIdsWithCustoms.toPNUUIDWithCustomList(),
        limit = 0
    )
).then {
    map(
        it,
        PNOperationType.MembershipOperation
    ) { pnMemberArrayResult -> pnMemberArrayResult.toSpaceMembershipResult() }
}
