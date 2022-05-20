package com.pubnub.entities

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ComposableRemoteAction.Companion.firstDo
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction.Companion.map
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.entities.models.consumer.space.PNRemoveSpaceResult
import com.pubnub.entities.models.consumer.space.PNSpaceArrayResult
import com.pubnub.entities.models.consumer.space.PNSpaceResult
import com.pubnub.entities.models.consumer.space.toPNRemoveSpaceResult
import com.pubnub.entities.models.consumer.space.toPNSpaceArrayResult
import com.pubnub.entities.models.consumer.space.toPNSpaceResult

fun PubNub.fetchSpaces(
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false
): ExtendedRemoteAction<PNSpaceArrayResult?> = firstDo(
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

fun PubNub.fetchSpace(
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

fun PubNub.createSpace(
    spaceId: String,
    name: String? = null,
    description: String? = null,
    custom: Any? = null,
    includeCustom: Boolean = false
): ExtendedRemoteAction<PNSpaceResult?> = firstDo(
    setChannelMetadata(
        channel = spaceId,
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

fun PubNub.updateSpace(
    spaceId: String,
    name: String? = null,
    description: String? = null,
    custom: Any? = null,
    includeCustom: Boolean = false
): ExtendedRemoteAction<PNSpaceResult?> = firstDo(
    setChannelMetadata(
        channel = spaceId,
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

fun PubNub.removeSpace(
    spaceId: String
): ExtendedRemoteAction<PNRemoveSpaceResult?> = firstDo(
    removeChannelMetadata(channel = spaceId)
).then {
    map(
        it,
        PNOperationType.SpaceOperation
    ) { pnRemoveMetadataResult -> pnRemoveMetadataResult.toPNRemoveSpaceResult() }
}
