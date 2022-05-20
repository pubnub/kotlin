package com.pubnub.entities.models.consumer.membership

import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.entities.models.consumer.space.PNSpace
import com.pubnub.entities.models.consumer.space.toPNSpace
import com.pubnub.entities.models.consumer.user.PNUser
import com.pubnub.entities.models.consumer.user.toPNUser

data class Membership(
    val user: PNUser?,
    val space: PNSpace?,
    val custom: Any?,
    val updated: String,
    val eTag: String
)

internal fun PNChannelMembership.toPNUserMembership(userId: String): Membership {
    return Membership(
        user = PNUser(id = userId),
        space = channel?.toPNSpace(),
        custom = custom,
        updated = updated,
        eTag = eTag
    )
}

internal fun PNMember.toPNSpaceMembership(spaceId: String): Membership {
    return Membership(
        user = uuid?.toPNUser(),
        space = PNSpace(id = spaceId),
        custom = custom,
        updated = updated,
        eTag = eTag
    )
}
