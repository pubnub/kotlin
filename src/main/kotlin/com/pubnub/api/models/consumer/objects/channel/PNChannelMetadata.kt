package com.pubnub.api.models.consumer.objects.channel

import com.google.gson.InstanceCreator
import com.google.gson.annotations.SerializedName
import com.pubnub.api.utils.OptionalChange
import com.pubnub.api.utils.getOrDefaultOrNull
import com.pubnub.api.utils.value
import java.lang.reflect.Type

interface IPNChannelMetadata {
    val id: String
    val name: String?
    val description: String?
    val custom: Any?
    val updated: String?
    val eTag: String?
    val status: String?
    val type: String?
}

class PNChannelMetadataChangeInstanceCreator : InstanceCreator<PNChannelMetadataChange> {
    override fun createInstance(type: Type?): PNChannelMetadataChange {
        return PNChannelMetadataChange(id = "N/A")
    }
}

data class PNChannelMetadataChange(
    override val id: String,
    override val updated: String? = null,
    override val eTag: String? = null,
    @SerializedName("name")
    val nameChange: OptionalChange<String> = OptionalChange.Unchanged(),
    @SerializedName("description")
    val descriptionChange: OptionalChange<String> = OptionalChange.Unchanged(),
    @SerializedName("custom")
    val customChange: OptionalChange<Any> = OptionalChange.Unchanged(),
    @SerializedName("status")
    val statusChange: OptionalChange<String> = OptionalChange.Unchanged(),
    @SerializedName("type")
    val typeChange: OptionalChange<String> = OptionalChange.Unchanged()
) : IPNChannelMetadata {
    @Transient
    override val name: String? = nameChange.value
    @Transient
    override val description: String? = descriptionChange.value
    @Transient
    override val custom: Any? = customChange.value
    @Transient
    override val status: String? = statusChange.value
    @Transient
    override val type: String? = typeChange.value
    fun patch(channelMetadata: IPNChannelMetadata): IPNChannelMetadata {
        if (channelMetadata.id != id) {
            TODO()
        }

        return PNChannelMetadata(
            id = channelMetadata.id,
            updated = updated,
            eTag = eTag,
            name = nameChange.getOrDefaultOrNull(channelMetadata.name),
            description = descriptionChange.getOrDefaultOrNull(channelMetadata.description),
            custom = customChange.getOrDefaultOrNull(channelMetadata.custom),
            status = statusChange.getOrDefaultOrNull(channelMetadata.status),
            type = typeChange.getOrDefaultOrNull(channelMetadata.type)
        )
    }
}

data class PNChannelMetadata(
    override val id: String,
    override val name: String?,
    override val description: String?,
    override val custom: Any?,
    override val updated: String?,
    override val eTag: String?,
    override val type: String?,
    override val status: String?
) : IPNChannelMetadata
