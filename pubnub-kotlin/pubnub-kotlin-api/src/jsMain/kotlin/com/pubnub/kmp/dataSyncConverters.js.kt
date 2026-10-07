package com.pubnub.kmp

import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncChannelData
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEntityData
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncMembershipData
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncRelationshipData
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncSetEventType
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncUserData
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncChannelEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncEntityEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncRelationshipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncUserEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncChannelEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncEntityEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncRelationshipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncUserEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNUnknownDataSyncEventMessage
import PubNub as PubNubJs

private const val SOURCE_DATA_SYNC = "data-sync"

/**
 * Maps a JS SDK DataSync (`e=5`) event onto the Kotlin sealed leaves.
 *
 * Dispatch is on the wire `(event, type)` pair — the same key the JVM deserializer uses — rather than on
 * the JS-derived `objectType`, so both targets pick the same leaf for the same wire message. Unrecognized
 * pairs map to [PNUnknownDataSyncEventMessage] instead of throwing.
 */
internal fun PubNubJs.DataSyncEvent.toDataSyncEventResult(): PNDataSyncEventResult {
    return PNDataSyncEventResult(
        BasePubSubResult(
            channel,
            subscription,
            timetoken.toLong(),
            null,
            null
        ),
        message.toDataSyncEventMessage()
    )
}

private fun PubNubJs.DataSyncMessage.toDataSyncEventMessage(): PNDataSyncEventMessage {
    val source = source.unsafeCast<String?>() ?: SOURCE_DATA_SYNC
    val classVersion = classVersion?.toInt()
    val setEvent = when (event) {
        "create" -> PNDataSyncSetEventType.CREATE
        "update" -> PNDataSyncSetEventType.UPDATE
        else -> null
    }
    val raw: dynamic = data
    return when {
        setEvent != null && type == "user" -> PNSetDataSyncUserEventMessage(
            source,
            version,
            setEvent,
            type,
            className,
            classLevel,
            classVersion,
            PNDataSyncUserData(
                raw.id,
                raw.createdAt,
                raw.updatedAt,
                raw.eTag,
                raw.expiresAt,
                raw.status.unsafeCast<String?>(),
                payloadOf(raw)
            )
        )
        setEvent != null && type == "channel" -> PNSetDataSyncChannelEventMessage(
            source,
            version,
            setEvent,
            type,
            className,
            classLevel,
            classVersion,
            PNDataSyncChannelData(
                raw.id,
                raw.createdAt,
                raw.updatedAt,
                raw.eTag,
                raw.expiresAt,
                raw.status.unsafeCast<String?>(),
                payloadOf(raw)
            )
        )
        setEvent != null && type == "entity" -> PNSetDataSyncEntityEventMessage(
            source,
            version,
            setEvent,
            type,
            className,
            classLevel,
            classVersion,
            PNDataSyncEntityData(
                raw.id,
                raw.createdAt,
                raw.updatedAt,
                raw.eTag,
                raw.expiresAt,
                raw.status.unsafeCast<String?>(),
                payloadOf(raw)
            )
        )
        setEvent != null && type == "membership" -> PNSetDataSyncMembershipEventMessage(
            source,
            version,
            setEvent,
            type,
            className,
            classLevel,
            classVersion,
            PNDataSyncMembershipData(
                raw.id, raw.channelId.unsafeCast<String?>().orEmpty(), raw.userId.unsafeCast<String?>().orEmpty(),
                raw.createdAt, raw.updatedAt, raw.eTag, raw.expiresAt, raw.status.unsafeCast<String?>(), payloadOf(raw)
            )
        )
        setEvent != null && type == "relationship" -> PNSetDataSyncRelationshipEventMessage(
            source,
            version,
            setEvent,
            type,
            className,
            classLevel,
            classVersion,
            PNDataSyncRelationshipData(
                raw.id, raw.entityAId.unsafeCast<String?>().orEmpty(), raw.entityBId.unsafeCast<String?>().orEmpty(),
                raw.createdAt, raw.updatedAt, raw.eTag, raw.expiresAt, raw.status.unsafeCast<String?>(), payloadOf(raw)
            )
        )
        event == "delete" && type == "user" -> PNDeleteDataSyncUserEventMessage(
            source,
            version,
            type,
            className,
            classLevel,
            classVersion,
            raw.id,
            raw.deletedAt
        )
        event == "delete" && type == "channel" -> PNDeleteDataSyncChannelEventMessage(
            source,
            version,
            type,
            className,
            classLevel,
            classVersion,
            raw.id,
            raw.deletedAt
        )
        event == "delete" && type == "entity" -> PNDeleteDataSyncEntityEventMessage(
            source,
            version,
            type,
            className,
            classLevel,
            classVersion,
            raw.id,
            raw.deletedAt
        )
        event == "delete" && type == "membership" -> PNDeleteDataSyncMembershipEventMessage(
            source,
            version,
            type,
            className,
            classLevel,
            classVersion,
            raw.id,
            raw.deletedAt
        )
        event == "delete" && type == "relationship" -> PNDeleteDataSyncRelationshipEventMessage(
            source,
            version,
            type,
            className,
            classLevel,
            classVersion,
            raw.id,
            raw.deletedAt
        )
        else -> PNUnknownDataSyncEventMessage(
            source, version, event, type, className, classLevel, classVersion,
            buildMap {
                put("source", source)
                put("event", event)
                put("type", type)
                className?.let { put("className", it) }
                classLevel?.let { put("classLevel", it) }
                classVersion?.let { put("classVersion", it) }
            },
            data.unsafeCast<JsMap<Any?>?>()?.toMap()
        )
    }
}

private fun payloadOf(raw: dynamic): Map<String, Any?>? = raw.payload.unsafeCast<JsMap<Any?>?>()?.toMap()
