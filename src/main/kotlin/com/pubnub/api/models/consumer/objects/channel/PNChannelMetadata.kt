package com.pubnub.api.models.consumer.objects.channel

import com.google.gson.annotations.SerializedName

sealed class OptionalChange<T> {
    data class NoChange<T>(val nothing: String = "") : OptionalChange<T>()
    data class None<T>(val nothing: String = "") : OptionalChange<T>()
    data class Some<T>(val value: T) : OptionalChange<T>()
}

val <T> OptionalChange<T>.value: T?
    get() {
        return when (this) {
            is OptionalChange.NoChange -> null
            is OptionalChange.None -> null
            is OptionalChange.Some -> this.value
        }
    }

interface IPNChannelMetadata {
    val id: String
    val name: String?
    val description: String?
    val custom: Any?
    val updated: String?
    val eTag: String?
}

data class PNChannelMetadataChange(
    override val id: String,
    @SerializedName("name")
    val nameChange: OptionalChange<String> = OptionalChange.NoChange(),
    @SerializedName("description")
    val descriptionChange: OptionalChange<String> = OptionalChange.NoChange(),
    @SerializedName("custom")
    val customChange: OptionalChange<Any> = OptionalChange.NoChange(),
    @SerializedName("updated")
    val updatedChange: OptionalChange<String> = OptionalChange.NoChange(),
    @SerializedName("eTag")
    val eTagChange: OptionalChange<String> = OptionalChange.NoChange()
) : IPNChannelMetadata {
    @Transient
    override val name: String? = nameChange.value
    @Transient
    override val description: String? = descriptionChange.value
    @Transient
    override val custom: Any? = customChange.value
    @Transient
    override val updated: String? = updatedChange.value
    @Transient
    override val eTag: String? = eTagChange.value
}


data class PNChannelMetadata(
    val id: String,
    val name: String?,
    val description: String?,
    val custom: Any?,
    val updated: String?,
    val eTag: String?,
    val type: String?,
    val status: String?
)
