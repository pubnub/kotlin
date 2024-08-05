package com.pubnub.api.models.consumer.objects.channel

import com.pubnub.api.utils.PatchValue

data class PartialPNChannelMetadata(
    val id: String,
    val name: PatchValue<String?>?,
    val description: PatchValue<String?>?,
    val custom: PatchValue<Map<String, Any?>?>?,
    val updated: PatchValue<String>?,
    val eTag: PatchValue<String>?,
    val type: PatchValue<String?>?,
    val status: PatchValue<String?>?,
) {
    operator fun plus(update: PartialPNChannelMetadata): PartialPNChannelMetadata {
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
