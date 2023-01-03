package com.pubnub.api.models.server

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.pubnub.api.SpaceId
import com.pubnub.api.models.consumer.File
import com.pubnub.api.models.consumer.Message
import com.pubnub.api.models.consumer.MessageType

data class SubscribeMessage(
    @SerializedName("a")
    internal val shard: String?,

    @SerializedName("b")
    internal val subscriptionMatch: String?,

    @SerializedName("c")
    internal val channel: String?,

    @SerializedName("d")
    internal val payload: JsonElement?,

    @SerializedName("f")
    internal val flags: String?,

    @SerializedName("i")
    internal val issuingClientId: String?,

    @SerializedName("k")
    internal val subscribeKey: String?,

    @SerializedName("o")
    internal val originationMetadata: OriginationMetaData?,

    @SerializedName("p")
    internal val publishMetaData: PublishMetaData?,

    @SerializedName("u")
    internal val userMetadata: JsonElement?,

    @SerializedName("e")
    private val pnMessageTypeInt: Int?,

    @SerializedName("mt")
    private val userMessageTypeString: String?,

    @SerializedName("si")
    private val stringSpaceId: String?
) {

    internal val userMessageType: MessageType?
        get() = userMessageTypeString?.let { MessageType(it) }

    internal val pnMessageType: MessageType
        get() = MessageType.of(pnMessageTypeInt)

    internal val spaceId: SpaceId?
        get() = stringSpaceId?.let { SpaceId(it) }

    fun supportsEncryption() = pnMessageType.let { it is Message || it is File }
}
