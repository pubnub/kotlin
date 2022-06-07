package com.pubnub.entities.models.consumer.membership

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult

data class FetchMembershipsResult(
    val data: Collection<Membership>,
    val totalCount: Int?,
    val next: PNPage?,
    val prev: PNPage?
)

internal fun PNChannelMembershipArrayResult.toUserFetchMembershipsResult(userId: String): FetchMembershipsResult {
    val userMembershipList: Collection<Membership> = data.map { pnChannelMembership ->
        pnChannelMembership.toUserMembership(userId)
    }
    return FetchMembershipsResult(
        data = userMembershipList,
        totalCount = totalCount,
        next = next,
        prev = prev
    )
}

internal fun PNMemberArrayResult.toSpaceFetchMembershipResult(spaceId: String): FetchMembershipsResult {
    val spaceMemberships: Collection<Membership> = data.map { pnMember ->
        pnMember.toSpaceMembership(spaceId)
    }

    return FetchMembershipsResult(
        data = spaceMemberships,
        totalCount = totalCount,
        next = next,
        prev = prev
    )
}
