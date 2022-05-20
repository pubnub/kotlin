package com.pubnub.entities.models.consumer.membership

import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.entities.models.consumer.space.Space
import com.pubnub.entities.models.consumer.space.toPNSpace
import com.pubnub.entities.models.consumer.user.User
import com.pubnub.entities.models.consumer.user.toPNUser

data class Membership(
    val user: User?,
    val space: Space?,
    val custom: Any?,
    val updated: String,
    val eTag: String
)

internal fun PNChannelMembership.toPNUserMembership(userId: String): Membership {
    return Membership(
        user = User(id = userId),
        space = channel?.toPNSpace(),
        custom = custom,
        updated = updated,
        eTag = eTag
    )
}

internal fun PNMember.toPNSpaceMembership(spaceId: String): Membership {
    return Membership(
        user = uuid?.toPNUser(),
        space = Space(id = spaceId),
        custom = custom,
        updated = updated,
        eTag = eTag
    )
}
