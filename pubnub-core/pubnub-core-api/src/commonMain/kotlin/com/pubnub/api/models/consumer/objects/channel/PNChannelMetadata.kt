package com.pubnub.api.models.consumer.objects.channel

import com.pubnub.api.utils.PatchValue

data class PNChannelMetadata(
    val id: String,
    val name: PatchValue<String?>?,
    val description: PatchValue<String?>?,
    val custom: PatchValue<Map<String, Any?>?>?,
    val updated: PatchValue<String>?,
    val eTag: PatchValue<String>?,
    val type: PatchValue<String?>?,
    val status: PatchValue<String?>?,
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
