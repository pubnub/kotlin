@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package com.pubnub.internal.models

import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.access_manager.sum.SpacePermissions
import com.pubnub.api.models.consumer.access_manager.sum.UserPermissions
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNChannelPatternGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNChannelResourceGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNSpacePatternPermissionsGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNSpacePermissionsGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNUUIDPatternGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNUUIDResourceGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNUserPatternPermissionsGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNUserPermissionsGrant
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNMemberKey
import com.pubnub.api.models.consumer.objects.PNMembershipKey
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.SortField
import com.pubnub.api.models.consumer.objects.member.MemberInput
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.ChannelMembershipInput
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.presence.PNWhereNowResult
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.internal.SpaceId
import com.pubnub.internal.models.consumer.objects.member.PNMember
import com.pubnub.internal.models.consumer.pubsub.objects.PNDeleteChannelMetadataEventMessage
import com.pubnub.internal.models.consumer.pubsub.objects.PNDeleteMembershipEvent
import com.pubnub.internal.models.consumer.pubsub.objects.PNDeleteMembershipEventMessage
import com.pubnub.internal.models.consumer.pubsub.objects.PNDeleteUUIDMetadataEventMessage
import com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventMessage
import com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.internal.models.consumer.pubsub.objects.PNSetChannelMetadataEventMessage
import com.pubnub.internal.models.consumer.pubsub.objects.PNSetMembershipEvent
import com.pubnub.internal.models.consumer.pubsub.objects.PNSetMembershipEventMessage
import com.pubnub.internal.models.consumer.pubsub.objects.PNSetUUIDMetadataEventMessage

internal fun PNObjectEventResult.toApi(): com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult {
    return com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult(
        BasePubSubResult(
            channel = this.channel,
            subscription = this.subscription,
            timetoken = this.timetoken,
            userMetadata = this.userMetadata,
            publisher = this.publisher,
        ),
        this.extractedMessage.toApi(),
    )
}

private fun PNObjectEventMessage.toApi(): com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventMessage {
    return when (this) {
        is PNSetChannelMetadataEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNSetChannelMetadataEventMessage(
                source = this.source,
                version = this.version,
                event = this.event,
                type = this.type,
                data = this.data,
            )
        }

        is PNDeleteChannelMetadataEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNDeleteChannelMetadataEventMessage(
                source = this.source,
                version = this.version,
                event = this.event,
                type = this.type,
                channel = this.channel,
            )
        }

        is PNDeleteMembershipEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNDeleteMembershipEventMessage(
                source = this.source,
                version = this.version,
                event = this.event,
                type = this.type,
                data = this.data.toApi(),
            )
        }

        is PNDeleteUUIDMetadataEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNDeleteUUIDMetadataEventMessage(
                source = this.source,
                version = this.version,
                event = this.event,
                type = this.type,
                uuid = this.uuid,
            )
        }

        is PNSetMembershipEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEventMessage(
                source = this.source,
                version = this.version,
                event = this.event,
                type = this.type,
                data = this.data.toApi(),
            )
        }

        is PNSetUUIDMetadataEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNSetUUIDMetadataEventMessage(
                source = this.source,
                version = this.version,
                event = this.event,
                type = this.type,
                data = this.data.toApi(),
            )
        }
    }
}

private fun PNUUIDMetadata.toApi(): PNUUIDMetadata {
    return PNUUIDMetadata(
        id = this.id,
        name = this.name,
        externalId = this.externalId,
        profileUrl = this.profileUrl,
        email = this.email,
        custom = this.custom,
        updated = this.updated,
        eTag = this.eTag,
        type = this.type,
        status = this.status,
    )
}

private fun PNSetMembershipEvent.toApi(): com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEvent {
    return com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEvent(
        channel = this.channel,
        uuid = this.uuid,
        custom = this.custom,
        eTag = this.eTag,
        updated = this.updated,
        status = this.status,
    )
}

private fun PNDeleteMembershipEvent.toApi(): com.pubnub.api.models.consumer.pubsub.objects.PNDeleteMembershipEvent {
    return com.pubnub.api.models.consumer.pubsub.objects.PNDeleteMembershipEvent(this.channelId, this.uuid)
}

internal fun List<ChannelGrant>.toInternalChannelGrants(): List<com.pubnub.internal.models.consumer.access_manager.v3.ChannelGrant> {
    return map {
        it.toInternal()
    }
}

private fun ChannelGrant.toInternal(): com.pubnub.internal.models.consumer.access_manager.v3.ChannelGrant {
    return when (this) {
        is PNChannelResourceGrant -> {
            com.pubnub.internal.models.consumer.access_manager.v3.ChannelGrant.name(
                name = id,
                read = read,
                write = write,
                manage = manage,
                delete = delete,
                create = create,
                get = get,
                join = join,
                update = update,
            )
        }

        is PNChannelPatternGrant -> {
            com.pubnub.internal.models.consumer.access_manager.v3.ChannelGrant.pattern(
                pattern = id,
                read = read,
                write = write,
                manage = manage,
                delete = delete,
                create = create,
                get = get,
                join = join,
                update = update,
            )
        }

        else -> {
            throw IllegalStateException("Should never happen.")
        }
    }
}

internal fun List<ChannelGroupGrant>.toInternalChannelGroupGrants(): List<com.pubnub.internal.models.consumer.access_manager.v3.ChannelGroupGrant> {
    return map { it.toInternal() }
}

private fun ChannelGroupGrant.toInternal(): com.pubnub.internal.models.consumer.access_manager.v3.ChannelGroupGrant {
    return when (this) {
        is com.pubnub.api.models.consumer.access_manager.v3.PNChannelGroupResourceGrant -> {
            com.pubnub.internal.models.consumer.access_manager.v3.ChannelGroupGrant.id(
                id = id,
                read = read,
                manage = manage,
            )
        }

        is com.pubnub.api.models.consumer.access_manager.v3.PNChannelGroupPatternGrant -> {
            com.pubnub.internal.models.consumer.access_manager.v3.ChannelGroupGrant.pattern(
                pattern = id,
                read = read,
                manage = manage,
            )
        }

        else -> {
            throw IllegalStateException("Should never happen.")
        }
    }
}

internal fun List<UUIDGrant>.toInternalUuidGrants(): List<com.pubnub.internal.models.consumer.access_manager.v3.UUIDGrant> {
    return map {
        it.toInternal()
    }
}

private fun UUIDGrant.toInternal(): com.pubnub.internal.models.consumer.access_manager.v3.UUIDGrant {
    return when (this) {
        is PNUUIDResourceGrant -> {
            com.pubnub.internal.models.consumer.access_manager.v3.UUIDGrant.id(
                id = id,
                get = get,
                update = update,
                delete = delete,
            )
        }

        is PNUUIDPatternGrant -> {
            com.pubnub.internal.models.consumer.access_manager.v3.UUIDGrant.pattern(
                pattern = id,
                get = get,
                update = update,
                delete = delete,
            )
        }

        else -> {
            throw IllegalStateException("Should never happen.")
        }
    }
}

internal fun List<SpacePermissions>.toInternalSpacePermissions(): List<com.pubnub.internal.models.consumer.access_manager.sum.SpacePermissions> {
    return map {
        it.toInternal()
    }
}

private fun SpacePermissions.toInternal(): com.pubnub.internal.models.consumer.access_manager.sum.SpacePermissions {
    return when (this) {
        is PNSpacePermissionsGrant -> {
            com.pubnub.internal.models.consumer.access_manager.sum.SpacePermissions.id(
                spaceId = SpaceId(id),
                read = read,
                write = write,
                manage = manage,
                delete = delete,
                get = get,
                join = join,
                update = update,
            )
        }

        is PNSpacePatternPermissionsGrant -> {
            com.pubnub.internal.models.consumer.access_manager.sum.SpacePermissions.pattern(
                pattern = id,
                read = read,
                write = write,
                manage = manage,
                delete = delete,
                get = get,
                join = join,
                update = update,
            )
        }

        else -> {
            throw IllegalStateException("Should never happen.")
        }
    }
}

internal fun List<UserPermissions>.toInternalUserPermissions(): List<com.pubnub.internal.models.consumer.access_manager.sum.UserPermissions> {
    return map {
        it.toInternal()
    }
}

private fun UserPermissions.toInternal(): com.pubnub.internal.models.consumer.access_manager.sum.UserPermissions {
    return when (this) {
        is PNUserPermissionsGrant -> {
            com.pubnub.internal.models.consumer.access_manager.sum.UserPermissions.id(
                userId = UserId(id),
                get = get,
                update = update,
                delete = delete,
            )
        }

        is PNUserPatternPermissionsGrant -> {
            com.pubnub.internal.models.consumer.access_manager.sum.UserPermissions.pattern(
                pattern = id,
                get = get,
                update = update,
                delete = delete,
            )
        }

        else -> {
            throw IllegalStateException("Should never happen.")
        }
    }
}

internal fun <T : SortField, T2 : com.pubnub.internal.models.consumer.objects.SortField> Collection<PNSortKey<T>>.toInternalSortKeys(): Collection<com.pubnub.internal.models.consumer.objects.PNSortKey<T2>> {
    return map {
        it.toInternal()
    }
}

private fun <T : SortField, T2 : com.pubnub.internal.models.consumer.objects.SortField> PNSortKey<T>.toInternal(): com.pubnub.internal.models.consumer.objects.PNSortKey<T2> {
    @Suppress("UNCHECKED_CAST")
    val sortKey: T2 =
        when (this.key) {
            is PNKey -> {
                com.pubnub.internal.models.consumer.objects.PNKey.valueOf(this.key.name) as T2
            }

            is PNMembershipKey -> {
                com.pubnub.internal.models.consumer.objects.PNMembershipKey.valueOf(this.key.name) as T2
            }

            is PNMemberKey -> {
                com.pubnub.internal.models.consumer.objects.PNMemberKey.valueOf(this.key.name) as T2
            }

            else -> {
                throw IllegalStateException("Should never happen.")
            }
        }
    return when (this) {
        is PNSortKey.PNAsc -> com.pubnub.internal.models.consumer.objects.PNSortKey.PNAsc(sortKey)
        is PNSortKey.PNDesc -> com.pubnub.internal.models.consumer.objects.PNSortKey.PNDesc(sortKey)
    }
}

internal fun PNChannelDetailsLevel?.toInternal(): com.pubnub.internal.models.consumer.objects.membership.PNChannelDetailsLevel? {
    if (this == null) {
        return null
    }
    return com.pubnub.internal.models.consumer.objects.membership.PNChannelDetailsLevel.valueOf(this.name)
}

internal fun PNUUIDDetailsLevel?.toInternal(): com.pubnub.internal.models.consumer.objects.member.PNUUIDDetailsLevel? {
    if (this == null) {
        return null
    }
    return com.pubnub.internal.models.consumer.objects.member.PNUUIDDetailsLevel.valueOf(this.name)
}

internal fun List<ChannelMembershipInput>.toInternalChannelMemberships(): List<com.pubnub.internal.models.consumer.objects.membership.ChannelMembershipInput> {
    return map {
        it.toInternal()
    }
}

private fun ChannelMembershipInput.toInternal(): com.pubnub.internal.models.consumer.objects.membership.ChannelMembershipInput {
    return com.pubnub.internal.models.consumer.objects.membership.PNChannelMembership.Partial(
        channelId = channel,
        custom = custom,
        status = status,
    )
}

internal fun Collection<MemberInput>.toInternalMemberInputs(): List<com.pubnub.internal.models.consumer.objects.member.MemberInput> {
    return map {
        it.toInternal()
    }
}

private fun MemberInput.toInternal(): com.pubnub.internal.models.consumer.objects.member.MemberInput {
    return PNMember.Partial(
        uuidId = this.uuid,
        custom = custom,
        status = status,
    )
}

fun com.pubnub.internal.models.consumer.objects.membership.PNChannelMembershipArrayResult.toApi(): PNChannelMembershipArrayResult {
    return PNChannelMembershipArrayResult(
        status = status,
        data = data.map(PNChannelMembership.Companion::from),
        totalCount = totalCount,
        next = next,
        prev = prev,
    )
}

fun com.pubnub.internal.models.consumer.presence.PNWhereNowResult.toApi(): PNWhereNowResult {
    return PNWhereNowResult(channels)
}
