package com.pubnub.entities.models.consumer.membership

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult

data class MembershipsResult(
    val status: Int,
    val data: Collection<Membership>,
    val totalCount: Int?,
    val next: PNPage?,
    val prev: PNPage?
)

internal fun PNChannelMembershipArrayResult.toPNUserMembershipArrayResult(userId: String): MembershipsResult {
    val pnUserMembershipList: Collection<Membership> = data.map { pnChannelMembership ->
        pnChannelMembership.toPNUserMembership(userId)
    }
    return MembershipsResult(
        status = status,
        data = pnUserMembershipList,
        totalCount = totalCount,
        next = next,
        prev = prev
    )
}

internal fun PNMemberArrayResult.toPNSpaceMembershipArrayResult(spaceId: String): MembershipsResult {
    val pnSpaceMembershipList: Collection<Membership> = data.map { pnMember ->
        pnMember.toPNSpaceMembership(spaceId)
    }

    return MembershipsResult(
        status = status,
        data = pnSpaceMembershipList,
        totalCount = totalCount,
        next = next,
        prev = prev
    )
}
