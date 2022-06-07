package com.pubnub.entities

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ComposableRemoteAction.Companion.firstDo
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction.Companion.map
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.ResultSortKey
import com.pubnub.entities.models.consumer.space.RemoveSpaceResult
import com.pubnub.entities.models.consumer.space.SpaceKey
import com.pubnub.entities.models.consumer.space.SpaceResult
import com.pubnub.entities.models.consumer.space.SpacesResult
import com.pubnub.entities.models.consumer.space.toRemoveSpaceResult
import com.pubnub.entities.models.consumer.space.toSpaceResult
import com.pubnub.entities.models.consumer.space.toSpacesResult

/**
 * Returns a paginated list of Spaces metadata, optionally including the custom data object for each.
 *
 * @param limit Number of objects to return in the response.
 *              Default is 100, which is also the maximum value.
 *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
 * @param page Use for pagination.
 *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
 *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
 *                           Ignored if you also supply the start parameter.
 * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
 *               expression are returned.
 * @param sort List of properties to sort by. Available options are id, name, and updated.
 *             e.g. listOf(ResultSortKey.Desc(key = ResultKey.ID), listOf(ResultSortKey.Asc(key = ResultKey.NAME)
 *             @see [Asc], [Desc]
 * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
 *                     Default is `false`.
 * @param includeCustom Include respective additional fields in the response.
 */
fun PubNub.fetchSpaces(
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<ResultSortKey<SpaceKey>> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false
): ExtendedRemoteAction<SpacesResult?> = firstDo(
    getAllChannelMetadata(
        limit = limit,
        page = page,
        filter = filter,
        sort = toPNSortKey(sort),
        includeCount = includeCount,
        includeCustom = includeCustom
    )
).then {
    map(
        it,
        PNOperationType.SpaceOperation
    ) { pnChannelMetadataArrayResult -> pnChannelMetadataArrayResult.toSpacesResult() }
}

private fun toPNSortKey(sort: Collection<ResultSortKey<SpaceKey>>): Collection<PNSortKey<PNKey>> {
    val pnSortKeyList = sort.map { resultSortKey ->
        when (resultSortKey) {
            is ResultSortKey.Asc -> PNSortKey.PNAsc(key = resultSortKey.key.toPNSortKey())
            is ResultSortKey.Desc -> PNSortKey.PNDesc(key = resultSortKey.key.toPNSortKey())
        }
    }
    return pnSortKeyList
}

/**
 * Returns metadata for the specified Space, optionally including the custom data object for each.
 *
 * @param spaceId Space ID.
 * @param includeCustom Include respective additional fields in the response.
 */
fun PubNub.fetchSpace(
    spaceId: String,
    includeCustom: Boolean = false
): ExtendedRemoteAction<SpaceResult?> = firstDo(
    getChannelMetadata(channel = spaceId, includeCustom = includeCustom)
).then {
    map(
        it,
        PNOperationType.SpaceOperation
    ) { pnChannelMetadataResult -> pnChannelMetadataResult.toSpaceResult() }
}

/**
 * Create metadata for a Space in the database, optionally including the custom data object for each.
 *
 * @param spaceId Space ID.
 * @param name Name of a space.
 * @param description Description of a channel.
 * @param custom Object with supported data types.
 * @param includeCustom Include respective additional fields in the response.
 */
fun PubNub.createSpace(
    spaceId: String,
    name: String? = null,
    description: String? = null,
    custom: Map<String, Any>? = null,
    includeCustom: Boolean = false
): ExtendedRemoteAction<SpaceResult?> = firstDo(
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
    ) { pnChannelMetadataResult -> pnChannelMetadataResult.toSpaceResult() }
}

/**
 * Update existing metadata for a Space in the database, optionally including the custom data object for each.
 *
 * @param spaceId Space ID.
 * @param name Name of a space.
 * @param description Description of a channel.
 * @param custom Object with supported data types.
 * @param includeCustom Include respective additional fields in the response.
 */
fun PubNub.updateSpace(
    spaceId: String,
    name: String? = null,
    description: String? = null,
    custom: Map<String, Any>? = null,
    includeCustom: Boolean = false
): ExtendedRemoteAction<SpaceResult?> = firstDo(
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
    ) { pnChannelMetadataResult -> pnChannelMetadataResult.toSpaceResult() }
}

/**
 * Removes the metadata from a specified Space.
 *
 * @param spaceId Space ID.
 */
fun PubNub.removeSpace(
    spaceId: String
): ExtendedRemoteAction<RemoveSpaceResult?> = firstDo(
    removeChannelMetadata(channel = spaceId)
).then {
    map(
        it,
        PNOperationType.SpaceOperation
    ) { pnRemoveMetadataResult -> pnRemoveMetadataResult.toRemoveSpaceResult() }
}
