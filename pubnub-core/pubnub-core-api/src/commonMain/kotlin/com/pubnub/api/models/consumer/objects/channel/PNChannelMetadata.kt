package com.pubnub.api.models.consumer.objects.channel

data class PNChannelMetadata(
    val id: String,
    val name: String? = null,
    val description: String? = null,
    val custom: Map<String, Any?>? = null,
    val updated: String? = null,
    val eTag: String? = null,
    val type: String? = null,
    val status: String? = null,
) {
    operator fun plus(update: PartialPNChannelMetadata): PNChannelMetadata {
        return copy(
            name = update.name?.value ?: name,
            description = update.description?.value ?: description,
            custom = update.custom?.value ?: custom,
            updated = update.updated?.value ?: updated,
            eTag = update.eTag?.value ?: eTag,
            type = update.type?.value ?: type,
            status = update.status?.value ?: status,
        )
    }

    companion object {
        fun from(update: PartialPNChannelMetadata): PNChannelMetadata {
            return PNChannelMetadata(
                id = update.id,
                name = update.name?.value,
                description = update.description?.value,
                custom = update.custom?.value,
                updated = update.updated?.value,
                eTag = update.eTag?.value,
                type = update.type?.value,
                status = update.status?.value,
            )
        }
    }
}
