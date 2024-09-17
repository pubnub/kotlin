package com.pubnub.internal.models.server

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.pubnub.internal.models.server.pubsub.MessageType

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
    internal val messageTypeInt: Int?,
    @SerializedName("mt")
    internal val type: String?,
) {
//    fun supportsEncryption() = type in arrayOf(null, TYPE_MESSAGE, TYPE_FILES)
    fun supportsEncryption() =
        MessageType.of(messageTypeInt).let { it == MessageType.Message || it == MessageType.File }
}
