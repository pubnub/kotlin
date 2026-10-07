package com.pubnub.internal.models.consumer.pubsub.datasync

import com.google.gson.JsonDeserializer
import com.google.gson.annotations.JsonAdapter
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncSetEventType
import com.pubnub.internal.utils.PolymorphicDeserializer

/**
 * Internal Gson-annotated twin of the public [com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventMessage].
 *
 * Unlike the objects (`e=2`) twin, this one is NOT flat: it mirrors the `e=5` wire nesting, keeping the
 * discriminators under a nested [PNDataSyncEventMetadata] `metadata` object. [toApi] flattens `metadata.*`
 * onto the public sealed leaf.
 */
internal object DataSyncExtractedMessageDeserializer :
    JsonDeserializer<PNDataSyncEventMessage> by PolymorphicDeserializer.dispatchByNestedFieldsValues(
        parent = "metadata",
        fields = listOf("event", "type"),
        mappingFieldValuesToClass =
            mapOf(
                listOf("create", "user") to PNSetDataSyncUserEventMessage::class.java,
                listOf("update", "user") to PNSetDataSyncUserEventMessage::class.java,
                listOf("delete", "user") to PNDeleteDataSyncUserEventMessage::class.java,
                listOf("create", "channel") to PNSetDataSyncChannelEventMessage::class.java,
                listOf("update", "channel") to PNSetDataSyncChannelEventMessage::class.java,
                listOf("delete", "channel") to PNDeleteDataSyncChannelEventMessage::class.java,
                listOf("create", "entity") to PNSetDataSyncEntityEventMessage::class.java,
                listOf("update", "entity") to PNSetDataSyncEntityEventMessage::class.java,
                listOf("delete", "entity") to PNDeleteDataSyncEntityEventMessage::class.java,
                listOf("create", "membership") to PNSetDataSyncMembershipEventMessage::class.java,
                listOf("update", "membership") to PNSetDataSyncMembershipEventMessage::class.java,
                listOf("delete", "membership") to PNDeleteDataSyncMembershipEventMessage::class.java,
                listOf("create", "relationship") to PNSetDataSyncRelationshipEventMessage::class.java,
                listOf("update", "relationship") to PNSetDataSyncRelationshipEventMessage::class.java,
                listOf("delete", "relationship") to PNDeleteDataSyncRelationshipEventMessage::class.java,
            ),
        // Unrecognized (event, type) -> unknown leaf instead of a batch-killing throw (forward-compat).
        defaultClass = PNUnknownDataSyncEventMessage::class.java,
    )

internal data class PNDataSyncEventMetadata(
    val source: String? = null,
    val event: String? = null,
    val type: String? = null,
    val className: String? = null,
    val classLevel: String? = null,
    val classVersion: Int? = null,
)

internal data class PNDataSyncData(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val eTag: String,
    val expiresAt: String,
    val status: String? = null,
    val payload: Map<String, Any?>? = null,
    // Only present on membership events (metadata.type == "membership").
    val channelId: String? = null,
    val userId: String? = null,
    // Only present on relationship events (metadata.type == "relationship").
    val entityAId: String? = null,
    val entityBId: String? = null,
)

internal data class PNDataSyncDeletedData(
    val id: String,
    val deletedAt: String,
)

@JsonAdapter(DataSyncExtractedMessageDeserializer::class)
internal sealed class PNDataSyncEventMessage {
    abstract val version: String
    abstract val metadata: PNDataSyncEventMetadata
}

internal data class PNSetDataSyncUserEventMessage(
    override val version: String,
    override val metadata: PNDataSyncEventMetadata,
    val data: PNDataSyncData,
) : PNDataSyncEventMessage()

internal data class PNDeleteDataSyncUserEventMessage(
    override val version: String,
    override val metadata: PNDataSyncEventMetadata,
    val data: PNDataSyncDeletedData,
) : PNDataSyncEventMessage()

internal data class PNSetDataSyncChannelEventMessage(
    override val version: String,
    override val metadata: PNDataSyncEventMetadata,
    val data: PNDataSyncData,
) : PNDataSyncEventMessage()

internal data class PNDeleteDataSyncChannelEventMessage(
    override val version: String,
    override val metadata: PNDataSyncEventMetadata,
    val data: PNDataSyncDeletedData,
) : PNDataSyncEventMessage()

internal data class PNSetDataSyncEntityEventMessage(
    override val version: String,
    override val metadata: PNDataSyncEventMetadata,
    val data: PNDataSyncData,
) : PNDataSyncEventMessage()

internal data class PNDeleteDataSyncEntityEventMessage(
    override val version: String,
    override val metadata: PNDataSyncEventMetadata,
    val data: PNDataSyncDeletedData,
) : PNDataSyncEventMessage()

internal data class PNSetDataSyncMembershipEventMessage(
    override val version: String,
    override val metadata: PNDataSyncEventMetadata,
    val data: PNDataSyncData,
) : PNDataSyncEventMessage()

internal data class PNDeleteDataSyncMembershipEventMessage(
    override val version: String,
    override val metadata: PNDataSyncEventMetadata,
    val data: PNDataSyncDeletedData,
) : PNDataSyncEventMessage()

internal data class PNSetDataSyncRelationshipEventMessage(
    override val version: String,
    override val metadata: PNDataSyncEventMetadata,
    val data: PNDataSyncData,
) : PNDataSyncEventMessage()

internal data class PNDeleteDataSyncRelationshipEventMessage(
    override val version: String,
    override val metadata: PNDataSyncEventMetadata,
    val data: PNDataSyncDeletedData,
) : PNDataSyncEventMessage()

internal data class PNUnknownDataSyncEventMessage(
    override val version: String,
    override val metadata: PNDataSyncEventMetadata,
    val data: Map<String, Any?>? = null,
) : PNDataSyncEventMessage()

private const val SOURCE_DATA_SYNC = "data-sync"

private val PNDataSyncEventMetadata.eventOrEmpty: String get() = event.orEmpty()
private val PNDataSyncEventMetadata.typeOrEmpty: String get() = type.orEmpty()
private val PNDataSyncEventMetadata.sourceOrDefault: String get() = source ?: SOURCE_DATA_SYNC

private val PNDataSyncEventMetadata.setEventType: PNDataSyncSetEventType
    get() =
        when (event) {
            "create" -> PNDataSyncSetEventType.CREATE
            "update" -> PNDataSyncSetEventType.UPDATE
            // Dispatch only routes ("create"|"update", type) pairs to a Set leaf; every other verb goes
            // to PNUnknownDataSyncEventMessage. Reaching here means the dispatch map and this mapper
            // drifted — fail loudly (the processor catches this per-message and drops the one event).
            else -> error("unexpected DataSync Set event verb: $event")
        }

internal fun PNDataSyncEventMessage.toApi(): com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventMessage {
    val md = metadata
    return when (this) {
        is PNSetDataSyncUserEventMessage ->
            com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncUserEventMessage(
                source = md.sourceOrDefault,
                version = version,
                event = md.setEventType,
                type = md.typeOrEmpty,
                className = md.className,
                classLevel = md.classLevel,
                classVersion = md.classVersion,
                data = data.toUserData(),
            )

        is PNDeleteDataSyncUserEventMessage ->
            com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncUserEventMessage(
                source = md.sourceOrDefault,
                version = version,
                type = md.typeOrEmpty,
                className = md.className,
                classLevel = md.classLevel,
                classVersion = md.classVersion,
                id = data.id,
                deletedAt = data.deletedAt,
            )

        is PNSetDataSyncChannelEventMessage ->
            com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncChannelEventMessage(
                source = md.sourceOrDefault,
                version = version,
                event = md.setEventType,
                type = md.typeOrEmpty,
                className = md.className,
                classLevel = md.classLevel,
                classVersion = md.classVersion,
                data = data.toChannelData(),
            )

        is PNDeleteDataSyncChannelEventMessage ->
            com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncChannelEventMessage(
                source = md.sourceOrDefault,
                version = version,
                type = md.typeOrEmpty,
                className = md.className,
                classLevel = md.classLevel,
                classVersion = md.classVersion,
                id = data.id,
                deletedAt = data.deletedAt,
            )

        is PNSetDataSyncEntityEventMessage ->
            com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncEntityEventMessage(
                source = md.sourceOrDefault,
                version = version,
                event = md.setEventType,
                type = md.typeOrEmpty,
                className = md.className,
                classLevel = md.classLevel,
                classVersion = md.classVersion,
                data = data.toEntityData(),
            )

        is PNDeleteDataSyncEntityEventMessage ->
            com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncEntityEventMessage(
                source = md.sourceOrDefault,
                version = version,
                type = md.typeOrEmpty,
                className = md.className,
                classLevel = md.classLevel,
                classVersion = md.classVersion,
                id = data.id,
                deletedAt = data.deletedAt,
            )

        is PNSetDataSyncMembershipEventMessage ->
            com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncMembershipEventMessage(
                source = md.sourceOrDefault,
                version = version,
                event = md.setEventType,
                type = md.typeOrEmpty,
                className = md.className,
                classLevel = md.classLevel,
                classVersion = md.classVersion,
                data = data.toMembershipData(),
            )

        is PNDeleteDataSyncMembershipEventMessage ->
            com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncMembershipEventMessage(
                source = md.sourceOrDefault,
                version = version,
                type = md.typeOrEmpty,
                className = md.className,
                classLevel = md.classLevel,
                classVersion = md.classVersion,
                id = data.id,
                deletedAt = data.deletedAt,
            )

        is PNSetDataSyncRelationshipEventMessage ->
            com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncRelationshipEventMessage(
                source = md.sourceOrDefault,
                version = version,
                event = md.setEventType,
                type = md.typeOrEmpty,
                className = md.className,
                classLevel = md.classLevel,
                classVersion = md.classVersion,
                data = data.toRelationshipData(),
            )

        is PNDeleteDataSyncRelationshipEventMessage ->
            com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncRelationshipEventMessage(
                source = md.sourceOrDefault,
                version = version,
                type = md.typeOrEmpty,
                className = md.className,
                classLevel = md.classLevel,
                classVersion = md.classVersion,
                id = data.id,
                deletedAt = data.deletedAt,
            )

        is PNUnknownDataSyncEventMessage ->
            com.pubnub.api.models.consumer.pubsub.datasync.PNUnknownDataSyncEventMessage(
                source = md.sourceOrDefault,
                version = version,
                event = md.eventOrEmpty,
                type = md.typeOrEmpty,
                className = md.className,
                classLevel = md.classLevel,
                classVersion = md.classVersion,
                metadata = md.toMap(),
                data = data,
            )
    }
}

private fun PNDataSyncEventMetadata.toMap(): Map<String, Any?> =
    buildMap {
        source?.let { put("source", it) }
        event?.let { put("event", it) }
        type?.let { put("type", it) }
        className?.let { put("className", it) }
        classLevel?.let { put("classLevel", it) }
        classVersion?.let { put("classVersion", it) }
    }

private fun PNDataSyncData.toUserData() =
    com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncUserData(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        eTag = eTag,
        expiresAt = expiresAt,
        status = status,
        payload = payload,
    )

private fun PNDataSyncData.toChannelData() =
    com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncChannelData(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        eTag = eTag,
        expiresAt = expiresAt,
        status = status,
        payload = payload,
    )

private fun PNDataSyncData.toEntityData() =
    com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEntityData(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        eTag = eTag,
        expiresAt = expiresAt,
        status = status,
        payload = payload,
    )

private fun PNDataSyncData.toMembershipData() =
    com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncMembershipData(
        id = id, channelId = channelId.orEmpty(), userId = userId.orEmpty(),
        createdAt = createdAt, updatedAt = updatedAt, eTag = eTag,
        expiresAt = expiresAt, status = status, payload = payload,
    )

private fun PNDataSyncData.toRelationshipData() =
    com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncRelationshipData(
        id = id, entityAId = entityAId.orEmpty(), entityBId = entityBId.orEmpty(),
        createdAt = createdAt, updatedAt = updatedAt, eTag = eTag,
        expiresAt = expiresAt, status = status, payload = payload,
    )
