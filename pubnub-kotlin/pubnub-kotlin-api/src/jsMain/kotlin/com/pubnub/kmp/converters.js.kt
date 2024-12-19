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
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

internal fun SetChannelMetadataResponse.toChannelMetadataResult(): PNChannelMetadataResult {
    return PNChannelMetadataResult(
        status.toInt(),
        data.toChannelMetadata()
    )
}

private fun <T> patchValueOf(any: T): PatchValue<T>? {
    return if (any === undefined) {
        null
    } else {
        PatchValue.of(any)
    }
}

@OptIn(ExperimentalContracts::class)
fun <T, R> T.letIfDefined(block: (T?) -> R): R? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (this === undefined) {
        return undefined
    }
    return block(this)
}

internal fun PubNub.ChannelMetadataObject.toChannelMetadata(): PNChannelMetadata {
    return PNChannelMetadata(
        id,
        patchValueOf(name),
        patchValueOf(description),
        patchValueOf(custom.letIfDefined { it?.toMap() }),
        patchValueOf(updated),
        patchValueOf(eTag),
        patchValueOf(type),
        patchValueOf(status),
    )
}

internal fun PubNub.UUIDMembershipObject.toPNMember() = PNMember(
    PNUUIDMetadata(
        uuid.id,
        patchValueOf(uuid.name), // TODO support optionals
        patchValueOf(uuid.externalId),
        patchValueOf(uuid.profileUrl),
        patchValueOf(uuid.email),
        patchValueOf(uuid.custom.letIfDefined { it?.toMap() }),
        patchValueOf(uuid.updated),
        patchValueOf(uuid.eTag),
        patchValueOf(uuid.type),
        patchValueOf(uuid.status),
    ),
    patchValueOf(custom.letIfDefined { it?.toMap() }),
    updated,
    eTag,
    status = patchValueOf(status),
    type = patchValueOf(type),
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
            patchValueOf(it.custom.letIfDefined { it?.toMap() }),
            it.updated,
            it.eTag,
            patchValueOf(it.status),
            patchValueOf(it.type),
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
    patchValueOf(name),
    patchValueOf(externalId),
    patchValueOf(profileUrl),
    patchValueOf(email),
    patchValueOf(custom.letIfDefined { it?.toMap() }),
    patchValueOf(updated),
    patchValueOf(eTag),
    patchValueOf(type),
    patchValueOf(status),
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
