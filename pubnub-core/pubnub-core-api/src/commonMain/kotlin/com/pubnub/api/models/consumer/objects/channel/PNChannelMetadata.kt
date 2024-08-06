package com.pubnub.api.models.consumer.objects.channel

import com.pubnub.api.utils.PatchValue

data class PNChannelMetadata(
    val id: String,
    val name: PatchValue<String?>? = null,
    val description: PatchValue<String?>? = null,
    val custom: PatchValue<Map<String, Any?>?>? = null,
    val updated: PatchValue<String>? = null,
    val eTag: PatchValue<String>? = null,
    val type: PatchValue<String?>? = null,
    val status: PatchValue<String?>? = null,
) {
    /**
     * Merge information from this `PNChannelMetadata` with new data from `update`, returning a new `PNChannelMetadata` instance.
     */
    operator fun plus(update: PNChannelMetadata): PNChannelMetadata {
        return copy(
            name = update.name ?: name,
            description = update.description ?: description,
            custom = update.custom ?: custom,
            updated = update.updated ?: updated,
            eTag = update.eTag ?: eTag,
            type = update.type ?: type,
            status = update.status ?: status,
        )
    }
}
