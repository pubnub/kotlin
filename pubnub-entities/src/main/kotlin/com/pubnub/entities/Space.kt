package com.pubnub.entities

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ComposableRemoteAction.Companion.firstDo
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction.Companion.map
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.entities.model.PNRemoveSpaceResult
import com.pubnub.entities.model.PNSpace
import com.pubnub.entities.model.PNSpaceArrayResult
import com.pubnub.entities.model.PNSpaceResult

fun PubNub.getSpaces(
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false
) : ExtendedRemoteAction<PNSpaceArrayResult?> = firstDo(
    getAllChannelMetadata(
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        includeCount = includeCount,
        includeCustom = includeCustom
    )
).then {
    map(
        it,
        PNOperationType.SpaceOperation
    ) { pnChannelMetadataArrayResult -> pnChannelMetadataArrayResult.toPNSpaceArrayResult() }
}

fun PubNub.getSpace(
    spaceId: String,
    includeCustom: Boolean = false
): ExtendedRemoteAction<PNSpaceResult?> = firstDo(
    getChannelMetadata(channel = spaceId, includeCustom = includeCustom)
).then {
    map(
        it,
        PNOperationType.SpaceOperation
    ) { pnChannelMetadataResult -> pnChannelMetadataResult.toPNSpaceResult() }
}


fun PubNub.setSpace(
    space: String,
    name: String? = null,
    description: String? = null,
    custom: Any? = null,
    includeCustom: Boolean = false
): ExtendedRemoteAction<PNSpaceResult?> = firstDo(
    setChannelMetadata(
        channel = space,
        name = name,
        description = description,
        custom = custom,
        includeCustom = includeCustom
    )
).then {
    map(
        it,
        PNOperationType.SpaceOperation
    ) { pnChannelMetadataResult -> pnChannelMetadataResult.toPNSpaceResult() }
}

fun PubNub.deleteSpace(
    spaceId: String
): ExtendedRemoteAction<PNRemoveSpaceResult?> = firstDo(
    removeChannelMetadata(channel = spaceId)
).then {
    map(
        it,
        PNOperationType.SpaceOperation
    ) { pnRemoveMetadataResult -> pnRemoveMetadataResult.toPNRemoveSpaceResult() }
}


internal fun PNChannelMetadataArrayResult?.toPNSpaceArrayResult(): PNSpaceArrayResult? {
    if (this == null) {
        return null
    }

    val pnSpaceList = data.map { pnChannelMetadata ->
        pnChannelMetadata.toPNSpace()
    }

    return PNSpaceArrayResult(status = status, data = pnSpaceList, totalCount = totalCount, next = next, prev = prev)
}

internal fun PNChannelMetadataResult?.toPNSpaceResult(): PNSpaceResult? {
    if (this == null) {
        return null
    }

    return PNSpaceResult(status = status, data = data?.toPNSpace())
}

internal fun PNChannelMetadata.toPNSpace(): PNSpace {
    return PNSpace(
        id = id,
        name = name,
        description = description,
        custom = custom,
        updated = updated,
        eTag = eTag
    )
}

internal fun PNRemoveMetadataResult?.toPNRemoveSpaceResult(): PNRemoveSpaceResult? {
    return this?.let { PNRemoveSpaceResult(status = it.status) }
}