package com.pubnub.api.models.consumer.pubsub.objects

import com.pubnub.api.models.consumer.objects.channel.PartialPNChannelMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PubSubResult

data class PNObjectEventResult(
    private val result: BasePubSubResult,
    val extractedMessage: PNObjectEventMessage,
) : PubSubResult by result

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
    val data: PartialPNChannelMetadata,
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
    val channel: String,
) : PNObjectEventMessage()

data class PNDeleteUUIDMetadataEventMessage(
    override val source: String,
    override val version: String,
    override val event: String,
    override val type: String,
    val uuid: String,
) : PNObjectEventMessage()

data class PNSetMembershipEvent(
    val channel: String,
    val uuid: String,
    val custom: Map<String, Any?>?,
    val eTag: String,
    val updated: String,
    val status: String?,
)

data class PNDeleteMembershipEvent(
    val channelId: String,
    val uuid: String,
)
