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
import com.pubnub.api.utils.PatchValue

internal fun SetChannelMetadataResponse.toChannelMetadataResult(): PNChannelMetadataResult {
    return PNChannelMetadataResult(
        status.toInt(),
        data.toChannelMetadata()
    )
}

internal fun PubNub.ChannelMetadataObject.toChannelMetadata(): PNChannelMetadata {
    return PNChannelMetadata(
        id,
        PatchValue.of(name), // TODO support optionals
        PatchValue.of(description),
        PatchValue.of(custom?.toMap()),
        PatchValue.of(updated),
        PatchValue.of(eTag),
        PatchValue.of(type),
        PatchValue.of(status),
    )
}

internal fun PubNub.UUIDMembershipObject.toPNMember() = PNMember(
    PNUUIDMetadata(
        uuid.id,
        PatchValue.of(uuid.name), // TODO support optionals
        PatchValue.of(uuid.externalId),
        PatchValue.of(uuid.profileUrl),
        PatchValue.of(uuid.email),
        PatchValue.of(uuid.custom?.toMap()),
        PatchValue.of(uuid.updated),
        PatchValue.of(uuid.eTag),
        PatchValue.of(uuid.type),
        PatchValue.of(uuid.status),
    ),
    PatchValue.of(custom?.toMap()),
    updated,
    eTag,
    PatchValue.of(status),
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
            it.channel.toChannelMetadata(),
            PatchValue.of(it.custom?.toMap()),
            it.updated,
            it.eTag,
            PatchValue.of(it.status),
        )
    },
    totalCount?.toInt(),
    next?.let { PNPage.PNNext(it) },
    prev?.let { PNPage.PNPrev(it) }
)

internal fun ObjectsResponse<PubNub.UUIDMetadataObject>.toPNUUIDMetadataResult() =
    PNUUIDMetadataResult(status.toInt(), data.toPNUUIDMetadata())

internal fun PubNub.UUIDMetadataObject.toPNUUIDMetadata() = PNUUIDMetadata(
    id,
    PatchValue.of(name),
    PatchValue.of(externalId),
    PatchValue.of(profileUrl),
    PatchValue.of(email),
    PatchValue.of(custom?.toMap()),
    PatchValue.of(updated),
    PatchValue.of(eTag),
    PatchValue.of(type),
    PatchValue.of(status),
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
