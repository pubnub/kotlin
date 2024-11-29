package com.pubnub.internal.models.consumer.pubsub.objects

import com.google.gson.JsonDeserializer
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.utils.PatchValue
import com.pubnub.internal.utils.PolymorphicDeserializer
import com.pubnub.internal.utils.UnwrapSingleField

internal object ObjectExtractedMessageDeserializer :
    JsonDeserializer<PNObjectEventMessage> by PolymorphicDeserializer.dispatchByFieldsValues(
        fields = listOf("event", "type"),
        mappingFieldValuesToClass =
            mapOf(
                listOf("set", "channel") to PNSetChannelMetadataEventMessage::class.java,
                listOf("set", "uuid") to PNSetUUIDMetadataEventMessage::class.java,
                listOf("set", "membership") to PNSetMembershipEventMessage::class.java,
                listOf("delete", "channel") to PNDeleteChannelMetadataEventMessage::class.java,
                listOf("delete", "uuid") to PNDeleteUUIDMetadataEventMessage::class.java,
                listOf("delete", "membership") to PNDeleteMembershipEventMessage::class.java,
            ),
    )

@JsonAdapter(ObjectExtractedMessageDeserializer::class)
sealed class PNObjectEventMessage {
    abstract val source: String
    abstract val version: String
    abstract val event: String
    abstract val type: String
}

data class PNSetChannelMetadataEventMessage(
    override val source: String,
    override val version: String,
    override val event: String,
    override val type: String,
    val data: PNChannelMetadata,
) : PNObjectEventMessage()

data class PNSetUUIDMetadataEventMessage(
    override val source: String,
    override val version: String,
    override val event: String,
    override val type: String,
    val data: PNUUIDMetadata,
) : PNObjectEventMessage()

data class PNSetMembershipEventMessage(
    override val source: String,
    override val version: String,
    override val event: String,
    override val type: String,
    val data: PNSetMembershipEvent,
) : PNObjectEventMessage()

data class PNDeleteMembershipEventMessage(
    override val source: String,
    override val version: String,
    override val event: String,
    override val type: String,
    val data: PNDeleteMembershipEvent,
) : PNObjectEventMessage()

data class PNDeleteChannelMetadataEventMessage(
    override val source: String,
    override val version: String,
    override val event: String,
    override val type: String,
    @JsonAdapter(UnwrapSingleField::class)
    @SerializedName("data")
    val channel: String,
) : PNObjectEventMessage()

data class PNDeleteUUIDMetadataEventMessage(
    override val source: String,
    override val version: String,
    override val event: String,
    override val type: String,
    @JsonAdapter(UnwrapSingleField::class)
    @SerializedName("data")
    val uuid: String,
) : PNObjectEventMessage()

data class PNSetMembershipEvent(
    @JsonAdapter(UnwrapSingleField::class)
    @SerializedName("channel")
    val channel: String,
    @JsonAdapter(UnwrapSingleField::class)
    val uuid: String,
    val custom: PatchValue<Map<String, Any?>?>?,
    val eTag: String,
    val updated: String,
    val status: PatchValue<String?>?,
    val type: PatchValue<String?>?,
)

data class PNDeleteMembershipEvent(
    @JsonAdapter(UnwrapSingleField::class)
    @SerializedName("channel")
    val channelId: String,
    @JsonAdapter(UnwrapSingleField::class)
    val uuid: String,
)

fun PNObjectEventMessage.toApi(): com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventMessage {
    return when (this) {
        is PNSetChannelMetadataEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNSetChannelMetadataEventMessage(
                source = this.source,
                version = this.version,
                event = this.event,
                type = this.type,
                data = this.data,
            )
        }

        is PNDeleteChannelMetadataEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNDeleteChannelMetadataEventMessage(
                source = this.source,
                version = this.version,
                event = this.event,
                type = this.type,
                channel = this.channel,
            )
        }

        is PNDeleteMembershipEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNDeleteMembershipEventMessage(
                source = this.source,
                version = this.version,
                event = this.event,
                type = this.type,
                data = this.data.toApi(),
            )
        }

        is PNDeleteUUIDMetadataEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNDeleteUUIDMetadataEventMessage(
                source = this.source,
                version = this.version,
                event = this.event,
                type = this.type,
                uuid = this.uuid,
            )
        }

        is PNSetMembershipEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEventMessage(
                source = this.source,
                version = this.version,
                event = this.event,
                type = this.type,
                data = this.data.toApi(),
            )
        }

        is PNSetUUIDMetadataEventMessage -> {
            com.pubnub.api.models.consumer.pubsub.objects.PNSetUUIDMetadataEventMessage(
                source = this.source,
                version = this.version,
                event = this.event,
                type = this.type,
                data = this.data,
            )
        }
    }
}

private fun PNSetMembershipEvent.toApi(): com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEvent {
    return com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEvent(
        channel = this.channel,
        uuid = this.uuid,
        custom = this.custom,
        eTag = this.eTag,
        updated = this.updated,
        status = this.status,
        type = this.type
    )
}

private fun PNDeleteMembershipEvent.toApi(): com.pubnub.api.models.consumer.pubsub.objects.PNDeleteMembershipEvent {
    return com.pubnub.api.models.consumer.pubsub.objects.PNDeleteMembershipEvent(this.channelId, this.uuid)
}
