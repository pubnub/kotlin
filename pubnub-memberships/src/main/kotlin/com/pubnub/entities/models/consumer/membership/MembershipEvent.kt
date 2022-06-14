package com.pubnub.entities.models.consumer.membership

import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEventMessage
import com.pubnub.entities.models.consumer.space.Space
import com.pubnub.entities.models.consumer.space.SpaceId
import com.pubnub.users.models.consumer.user.User
import com.pubnub.users.models.consumer.user.UserId

sealed class MembershipEvent {
    abstract val spaceId: SpaceId
    abstract val timetoken: Long
}

data class MembershipModified(
    override val spaceId: SpaceId,
    override val timetoken: Long,
    val data: Data
) : MembershipEvent() {
    data class Data(
        val space: Space,
        val user: User,
        val custom: Map<String, Any>?,
        val status: String?
    )
}

data class MembershipRemoved(
    override val spaceId: SpaceId,
    override val timetoken: Long,
    val data: Data
) : MembershipEvent() {
    data class Data(
        val space: Space,
        val user: User
    )
}

fun PNObjectEventResult.toMembershipEvent(): MembershipEvent? {
    return when (val m = extractedMessage) {
        is PNSetMembershipEventMessage -> {
            MembershipModified(
                data = MembershipModified.Data(
                    space = Space(id = SpaceId(m.data.channel)),
                    user = User(id = UserId(m.data.uuid)),
                    custom = m.data.custom as? Map<String, Any>,
                    status = m.data.status
                ),
                spaceId = SpaceId(channel), timetoken = timetoken ?: 0
            )
        }
        is PNDeleteMembershipEventMessage -> {
            MembershipRemoved(
                data = MembershipRemoved.Data(
                    space = Space(id = SpaceId(m.data.channelId)), user = User(id = UserId(m.data.uuid))
                ),
                spaceId = SpaceId(channel), timetoken = timetoken ?: 0
            )
        }
        else -> {
            return null
        }
    }
}
