package com.pubnub.entities

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ComposableRemoteAction.Companion.firstDo
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction.Companion.map
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.entities.models.consumer.membership.FetchMembershipsResult
import com.pubnub.entities.models.consumer.membership.MembershipsResult
import com.pubnub.entities.models.consumer.membership.SpaceDetailsLevel
import com.pubnub.entities.models.consumer.membership.SpaceIdWithCustom
import com.pubnub.entities.models.consumer.membership.UserDetailsLevel
import com.pubnub.entities.models.consumer.membership.UserIdWithCustom
import com.pubnub.entities.models.consumer.membership.toPNChannelDetailsLevel
import com.pubnub.entities.models.consumer.membership.toPNChannelWithCustomList
import com.pubnub.entities.models.consumer.membership.toPNUUIDDetailsLevel
import com.pubnub.entities.models.consumer.membership.toPNUUIDWithCustomList
import com.pubnub.entities.models.consumer.membership.toSpaceFetchMembershipResult
import com.pubnub.entities.models.consumer.membership.toSpaceMembershipResult
import com.pubnub.entities.models.consumer.membership.toUserFetchMembershipsResult
import com.pubnub.entities.models.consumer.membership.toUserMembershipsResult

fun PubNub.addMembershipOfUser(
    spaceIdWithCustomList: List<SpaceIdWithCustom>,
    userid: String = configuration.uuid,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeSpaceDetails: SpaceDetailsLevel? = null
): ExtendedRemoteAction<MembershipsResult> = firstDo(
    setMemberships(
        channels = spaceIdWithCustomList.toPNChannelWithCustomList(),
        uuid = userid,
        limit = 0,
        page = page,
        filter = filter,
        sort = sort,
        includeCount = false,
        includeCustom = includeCustom,
        includeChannelDetails = includeSpaceDetails?.toPNChannelDetailsLevel()
    )
).then {
    map(
        it,
        PNOperationType.MembershipOperation
    ) { pnChannelMembershipArrayResult -> pnChannelMembershipArrayResult.toUserMembershipsResult(userid) }
}

fun PubNub.addMembershipOfSpace(
    spaceId: String,
    userIdWithCustomList: List<UserIdWithCustom>,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeUserDetails: UserDetailsLevel? = null
): ExtendedRemoteAction<MembershipsResult> = firstDo(
    setChannelMembers(
        channel = spaceId,
        uuids = userIdWithCustomList.toPNUUIDWithCustomList(),
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        includeCount = includeCount,
        includeCustom = includeCustom,
        includeUUIDDetails = includeUserDetails?.toPNUUIDDetailsLevel()
    )
).then {
    map(
        it,
        PNOperationType.MembershipOperation
    ) { pnMemberArrayResult -> pnMemberArrayResult.toSpaceMembershipResult(spaceId) }
}

fun PubNub.fetchMembershipsOfUser(
    userId: String = configuration.uuid,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeSpaceDetails: SpaceDetailsLevel? = null
): ExtendedRemoteAction<FetchMembershipsResult?> = firstDo(
    getMemberships(
        uuid = userId,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
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

fun PubNub.fetchMembershipsOfSpace(
    spaceId: String,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeUserDetails: UserDetailsLevel? = null
): ExtendedRemoteAction<FetchMembershipsResult?> = firstDo(
    getChannelMembers(
        channel = spaceId,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
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

fun PubNub.removeMembershipOfUser(
    spaceIds: List<String>,
    userId: String = configuration.uuid,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeSpaceDetails: SpaceDetailsLevel? = null  //TODO jak to działa? jaki jest rezultat jak to jest włączone/wyłączone
): ExtendedRemoteAction<MembershipsResult> = firstDo(
    removeMemberships(
        channels = spaceIds,
        uuid = userId,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        includeCount = includeCount,
        includeCustom = includeCustom,
        includeChannelDetails = includeSpaceDetails?.toPNChannelDetailsLevel()
    )
).then {
    map(
        it,
        PNOperationType.MembershipOperation
    ) { pnChannelMembershipArrayResult -> pnChannelMembershipArrayResult.toUserMembershipsResult(userId) }
}

fun PubNub.removeMembershipOfSpace(
    spaceId: String,
    userIds: List<String>,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeUserDetails: UserDetailsLevel? = null
): ExtendedRemoteAction<MembershipsResult> = firstDo(
    removeChannelMembers(
        channel = spaceId,
        uuids = userIds,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        includeCount = includeCount,
        includeCustom = includeCustom,
        includeUUIDDetails = includeUserDetails?.toPNUUIDDetailsLevel()
    )
).then {
    map(
        it,
        PNOperationType.MembershipOperation
    ) { pnMemberArrayResult -> pnMemberArrayResult.toSpaceMembershipResult(spaceId) }
}