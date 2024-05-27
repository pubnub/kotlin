package com.pubnub.kmp

import ManageChannelMembersResponse
import ManageMembershipsResponse
import ObjectsResponse
import PubNub
import SetChannelMetadataResponse
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult

internal fun SetChannelMetadataResponse.toChannelMetadataResult(): PNChannelMetadataResult {
    return PNChannelMetadataResult(
        status.toInt(),
        data.toChannelMetadata()
    )
}

internal fun PubNub.ChannelMetadataObject.toChannelMetadata(): PNChannelMetadata {
    return PNChannelMetadata(
        id,
        name,
        description,
        custom?.toMap(),
        updated,
        eTag,
        type,
        status
    )
}

internal fun PubNub.UUIDMembershipObject.toPNMember() = PNMember(
    PNUUIDMetadata(
        uuid.id,
        uuid.name,
        uuid.externalId,
        uuid.profileUrl,
        uuid.email,
        uuid.custom?.toMap(),
        uuid.updated,
        uuid.eTag,
        uuid.type,
        uuid.status
    ),
    custom?.toMap(),
    updated,
    eTag,
    status
)

internal fun ManageChannelMembersResponse.toPNMemberArrayResult() = PNMemberArrayResult(
    status.toInt(),
    data.map(PubNub.UUIDMembershipObject::toPNMember),
    totalCount?.toInt(),
    next?.let { next -> PNPage.PNNext(next) },
    prev?.let { prev -> PNPage.PNPrev(prev) },
)

internal fun ManageMembershipsResponse.toPNChannelMembershipArrayResult() = PNChannelMembershipArrayResult(
    status.toInt(),
    data.map {
        PNChannelMembership(
            it.channel?.toChannelMetadata(),
            it.custom?.toMap(),
            it.updated,
            it.eTag,
            it.status
        )
    },
    totalCount?.toInt(),
    next?.let { PNPage.PNNext(it) },
    prev?.let { PNPage.PNPrev(it) }
)

internal fun ObjectsResponse<PubNub.UUIDMetadataObject>.toPNUUIDMetadataResult() =
    PNUUIDMetadataResult(status.toInt(), data.toPNUUIDMetadata())
internal fun PubNub.UUIDMetadataObject.toPNUUIDMetadata() = PNUUIDMetadata(
    id, name, externalId, profileUrl, email, custom?.toMap(), updated, eTag, type, status
)

internal fun PubNub.MessageAction.toMessageAction() =
    PNMessageAction(
        type,
        value,
        messageTimetoken.toLong()
    ).apply {
        this.actionTimetoken = this@toMessageAction.actionTimetoken.toLong()
        this.uuid = this@toMessageAction.uuid
    }