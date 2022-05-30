package com.pubnub.entities.models.consumer.membership

import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult

data class MembershipsResult(
    val status: Int
)

internal fun PNChannelMembershipArrayResult.toUserMembershipsResult(): MembershipsResult {
    return MembershipsResult(
        status = status
    )
}

internal fun PNMemberArrayResult.toSpaceMembershipResult(): MembershipsResult {
    return MembershipsResult(
        status = status
    )
}
