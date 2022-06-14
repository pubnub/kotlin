package com.pubnub.entities.models.consumer.membership

import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.entities.models.consumer.space.Space
import com.pubnub.entities.models.consumer.space.SpaceId
import com.pubnub.entities.models.consumer.space.toSpace
import com.pubnub.user.models.consumer.user.User
import com.pubnub.user.models.consumer.user.UserId
import com.pubnub.user.models.consumer.user.toUser

data class Membership(
    val user: User?,
    val space: Space?,
    val custom: Map<String, Any>?,
    val updated: String? = null,
    val eTag: String? = null,
    val status: String?
) {

    companion object {
        fun Partial(
            userId: UserId,
            custom: Map<String, Any>? = null,
            status: String? = null
        ): PartialWithUser {
            return PartialWithUser(userId = userId, custom = custom, status = status)
        }

        fun Partial(
            spaceId: SpaceId,
            custom: Map<String, Any>? = null,
            status: String? = null
        ): PartialWithSpace {
            return PartialWithSpace(spaceId = spaceId, custom = custom, status = status)
        }
    }

    data class PartialWithUser internal constructor(
        val userId: UserId,
        val custom: Map<String, Any>? = null,
        val status: String? = null
    )

    data class PartialWithSpace internal constructor(
        val spaceId: SpaceId,
        val custom: Map<String, Any>? = null,
        val status: String? = null
    )
}

internal fun PNChannelMembership.toUserMembership(userId: UserId): Membership {
    return Membership(
        user = User(id = userId),
        space = channel?.toSpace(),
        custom = custom as Map<String, Any>?,
        updated = updated,
        eTag = eTag,
        status = status
    )
}

internal fun PNMember.toSpaceMembership(spaceId: SpaceId): Membership {
    return Membership(
        user = uuid?.toUser(),
        space = Space(id = spaceId),
        custom = custom as Map<String, Any>?,
        updated = updated,
        eTag = eTag,
        status = status
    )
}

internal fun List<Membership.PartialWithSpace>.toPNChannelMembershipPartialList(): List<PNChannelMembership.Partial> {
    return map { it.toPNChannelMembershipPartial() }
}

internal fun Membership.PartialWithSpace.toPNChannelMembershipPartial(): PNChannelMembership.Partial {
    return PNChannelMembership.Partial(channelId = spaceId.value, custom = custom, status = status)
}

internal fun List<Membership.PartialWithUser>.toPNMemberPartialList(): List<PNMember.Partial> {
    return map { it.toPNMemberPartial() }
}

internal fun Membership.PartialWithUser.toPNMemberPartial(): PNMember.Partial {
    return PNMember.Partial(uuidId = userId.value, custom = custom, status = status)
}
