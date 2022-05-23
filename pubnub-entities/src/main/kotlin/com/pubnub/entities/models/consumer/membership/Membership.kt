package com.pubnub.entities.models.consumer.membership

import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.entities.models.consumer.space.Space
import com.pubnub.entities.models.consumer.space.toSpace
import com.pubnub.entities.models.consumer.user.User
import com.pubnub.entities.models.consumer.user.toUser

data class Membership(
    val user: User?,
    val space: Space?,
    val custom: Any?,
    val updated: String,
    val eTag: String
)

//this comment will be removed
internal fun PNChannelMembership.toUserMembership(userId: String): Membership {
    return Membership(
        user = User(id = userId),
        space = channel?.toSpace(),
        custom = custom,
        updated = updated,
        eTag = eTag
    )
}

internal fun PNMember.toSpaceMembership(spaceId: String): Membership {
    return Membership(
        user = uuid?.toUser(),
        space = Space(id = spaceId),
        custom = custom,
        updated = updated,
        eTag = eTag
    )
}
