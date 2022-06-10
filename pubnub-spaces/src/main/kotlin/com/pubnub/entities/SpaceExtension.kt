package com.pubnub.entities

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.endpoints.remoteaction.ComposableRemoteAction.Companion.firstDo
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction.Companion.map
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.ResultSortKey
import com.pubnub.api.models.consumer.objects.channel.OptionalChange
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteChannelMetadataEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.models.consumer.pubsub.objects.PNSetChannelMetadataEventMessage
import com.pubnub.entities.models.consumer.space.RemoveSpaceResult
import com.pubnub.entities.models.consumer.space.Space
import com.pubnub.entities.models.consumer.space.SpaceKey
import com.pubnub.entities.models.consumer.space.SpacesResult
import com.pubnub.entities.models.consumer.space.toRemoveSpaceResult
import com.pubnub.entities.models.consumer.space.toSpace
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
        it, PNOperationType.SpaceOperation
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
): ExtendedRemoteAction<Space?> = firstDo(
    getChannelMetadata(channel = spaceId, includeCustom = includeCustom)
).then {
    map(
        it, PNOperationType.SpaceOperation
    ) { pnChannelMetadataResult -> pnChannelMetadataResult.toSpace() }
}

/**
 * Create metadata for a Space in the database, optionally including the custom data object for each.
 *
 * @param spaceId Space ID.
 * @param name Name of a space.
 * @param description Description of a channel.
 * @param custom Object with supported data types.
 * @param includeCustom Include respective additional fields in the response.
 * @param type Type of the space.
 * @param status Status of the space.
 */
fun PubNub.createSpace(
    spaceId: String,
    name: String? = null,
    description: String? = null,
    custom: Map<String, Any>? = null,
    includeCustom: Boolean = false,
    type: String? = null,
    status: String? = null
): ExtendedRemoteAction<Space?> = firstDo(
    setChannelMetadata(
        channel = spaceId,
        name = name,
        description = description,
        custom = custom,
        includeCustom = includeCustom,
        type = type,
        status = status
    )
).then {
    map(
        it, PNOperationType.SpaceOperation
    ) { pnChannelMetadataResult -> pnChannelMetadataResult.toSpace() }
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
    includeCustom: Boolean = false,
    type: String? = null,
    status: String? = null
): ExtendedRemoteAction<Space?> = firstDo(
    setChannelMetadata(
        channel = spaceId,
        name = name,
        description = description,
        custom = custom,
        includeCustom = includeCustom,
        type = type,
        status = status
    )
).then {
    map(
        it, PNOperationType.SpaceOperation
    ) { pnChannelMetadataResult -> pnChannelMetadataResult.toSpace() }
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
        it, PNOperationType.SpaceOperation
    ) { pnRemoveMetadataResult -> pnRemoveMetadataResult.toRemoveSpaceResult() }
}

interface Stoppable {
    fun stop()
}

class StoppableListener(
    private val pubNub: PubNub,
    private val listener: Listener
) : Stoppable, Listener by listener {
    override fun stop() {
        pubNub.removeListener(listener)
    }
}

//PNObjectEventResult(result=BasePubSubResult(channel=ThisIsMyChannel71340D6011, subscription=null, timetoken=16541757313672192, userMetadata=null, publisher=null), extractedMessage=PNDeleteMembershipEventMessage(source=objects, version=2.0, event=delete, type=membership, data=PNDeleteMembershipEvent(channelId=ThisIsMyChannel71340D6011, uuid=client-6be06195-dde5-419f-9a9f-5d5e798c26b6)))

data class ExtractedMessage(
    val source: String,
    val version: String,
    val event: String,
    val type: String
)

sealed class SpaceEventData {
    abstract val event: String

    data class SpaceModified(val spaceId: String,
    val name: OptionalChange) : SpaceEventData() {
        override val event: String = "modified"
    }

    data class SpaceRemoved(val spaceId: String) : SpaceEventData() {
        override val event: String = "removed"
    }
}

data class Message(val data: SpaceEventData) {
    val type: String = "space"
    val event: String = data.event
}

data class SpaceEvent(
    val spaceId: String,
    val timetoken: Long,
    val message: Message
)

fun JsonElement.toMap(): Map<String, Any> {
    val map = mutableMapOf<String, Any>()

    when (this) {
        is JsonObject -> {
            this.keySet().forEach {
                when (val element = this.get(it)) {
                    is JsonPrimitive -> {
                        when {
                            element.isBoolean -> {
                                map[it] = element.asBoolean
                            }
                            element.isNumber -> {
                                map[it] = element.asNumber
                            }
                            element.isString -> {
                                map[it] = element.asString
                            }
                        }
                    }
                }
            }
        }
    }

    return map
}

fun PNObjectEventResult.toSpaceEvent(): SpaceEvent? {
    val message = when (val m = extractedMessage) {
        is PNSetChannelMetadataEventMessage -> {
            Message(
                SpaceEventData.SpaceModified(
                    spaceId = m.source
                )
            )
        }
        is PNDeleteChannelMetadataEventMessage -> {
            Message(
                SpaceEventData.SpaceRemoved(
                    spaceId = m.source
                )
            )
        }
        else -> {
            return null
        }
    }

    return SpaceEvent(
        spaceId = channel,
        timetoken = timetoken ?: 0,
        message = message
    )
}

fun PubNub.addSpaceEventsListener(block: (SpaceEvent) -> Unit): Stoppable {
    val listener = object : SubscribeCallback() {
        override fun status(pubnub: PubNub, pnStatus: PNStatus) {
        }

        override fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {
            objectEvent.toSpaceEvent()?.let(block)
        }
    }
    addListener(listener)
    return StoppableListener(this, listener)
}
