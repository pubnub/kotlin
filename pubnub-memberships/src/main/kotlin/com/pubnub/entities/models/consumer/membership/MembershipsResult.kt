package com.pubnub.entities.models.consumer.membership

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.entities.models.consumer.space.SpaceId
import com.pubnub.users.models.consumer.user.UserId

data class MembershipsResult(
    val data: Collection<Membership>,
    val totalCount: Int?,
    val next: PNPage?,
    val prev: PNPage?
)

internal fun PNChannelMembershipArrayResult.toUserFetchMembershipsResult(userId: UserId): MembershipsResult {
    val userMembershipList: Collection<Membership> = data.map { pnChannelMembership ->
        pnChannelMembership.toUserMembership(userId)
    }
    return MembershipsResult(
        data = userMembershipList,
        totalCount = totalCount,
        next = next,
        prev = prev
    )
}

internal fun PNMemberArrayResult.toSpaceFetchMembershipResult(spaceId: SpaceId): MembershipsResult {
    val spaceMemberships: Collection<Membership> = data.map { pnMember ->
        pnMember.toSpaceMembership(spaceId)
    }

    return MembershipsResult(
        data = spaceMemberships,
        totalCount = totalCount,
        next = next,
        prev = prev
    )
}
