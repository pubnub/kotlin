package com.pubnub.internal.models.consumer.objects

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
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
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
            this.channel,
            this.subscription,
            this.timetoken,
            this.userMetadata,
            this.publisher
        ),
        this.extractedMessage.toApi()
    )
}

private fun PNObjectEventMessage.toApi(): com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventMessage {
    return when (this) {
        is PNSetChannelMetadataEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNSetChannelMetadataEventMessage(
                this.source,
                this.version,
                this.event,
                this.type,
                this.data
            )
        }

        is PNDeleteChannelMetadataEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNDeleteChannelMetadataEventMessage(
                this.source,
                this.version,
                this.event,
                this.type,
                this.channel
            )
        }

        is PNDeleteMembershipEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNDeleteMembershipEventMessage(
                this.source,
                this.version,
                this.event,
                this.type,
                this.data.toApi()
            )
        }

        is PNDeleteUUIDMetadataEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNDeleteUUIDMetadataEventMessage(
                this.source,
                this.version,
                this.event,
                this.type,
                this.uuid
            )
        }

        is PNSetMembershipEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEventMessage(
                this.source,
                this.version,
                this.event,
                this.type,
                this.data.toApi()
            )
        }

        is PNSetUUIDMetadataEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNSetUUIDMetadataEventMessage(
                this.source,
                this.version,
                this.event,
                this.type,
                this.data.toApi()
            )
        }
    }
}

private fun PNUUIDMetadata.toApi(): PNUUIDMetadata {
    return PNUUIDMetadata(
        this.id,
        this.name,
        this.externalId,
        this.profileUrl,
        this.email,
        this.custom,
        this.updated,
        this.eTag,
        this.type,
        this.status
    )
}

private fun PNSetMembershipEvent.toApi(): com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEvent {
    return com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEvent(
        this.channel,
        this.uuid,
        this.custom,
        this.eTag,
        this.updated,
        this.status
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
                id,
                read,
                write,
                manage,
                delete,
                create,
                get,
                join,
                update
            )
        }

        is PNChannelPatternGrant -> {
            com.pubnub.internal.models.consumer.access_manager.v3.ChannelGrant.pattern(
                id,
                read,
                write,
                manage,
                delete,
                create,
                get,
                join,
                update
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
                id, read, manage
            )
        }

        is com.pubnub.api.models.consumer.access_manager.v3.PNChannelGroupPatternGrant -> {
            com.pubnub.internal.models.consumer.access_manager.v3.ChannelGroupGrant.pattern(
                id, read, manage
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
                id, get, update, delete
            )
        }

        is PNUUIDPatternGrant -> {
            com.pubnub.internal.models.consumer.access_manager.v3.UUIDGrant.pattern(
                id, get, update, delete
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
                SpaceId(id), read, write, manage, delete, get, join, update
            )
        }

        is PNSpacePatternPermissionsGrant -> {
            com.pubnub.internal.models.consumer.access_manager.sum.SpacePermissions.pattern(
                id, read, write, manage, delete, get, join, update
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
        is com.pubnub.api.models.consumer.access_manager.v3.PNUserPermissionsGrant -> {
            com.pubnub.internal.models.consumer.access_manager.sum.UserPermissions.id(
                UserId(id), get, update, delete
            )
        }

        is com.pubnub.api.models.consumer.access_manager.v3.PNUserPatternPermissionsGrant -> {
            com.pubnub.internal.models.consumer.access_manager.sum.UserPermissions.pattern(
                id, get, update, delete
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
    val sortKey: T2 = when (this.key) {
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
        return null;
    }
    return com.pubnub.internal.models.consumer.objects.membership.PNChannelDetailsLevel.valueOf(this.name)
}

internal fun PNUUIDDetailsLevel?.toInternal(): com.pubnub.internal.models.consumer.objects.member.PNUUIDDetailsLevel? {
    if (this == null) {
        return null;
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
        channel, custom, status
    )
}

internal fun Collection<MemberInput>.toInternalMemberInputs(): List<com.pubnub.internal.models.consumer.objects.member.MemberInput> {
    return map {
        it.toInternal()
    }
}

private fun MemberInput.toInternal(): com.pubnub.internal.models.consumer.objects.member.MemberInput {
    return PNMember.Partial(
        this.uuid, custom, status
    )
}
