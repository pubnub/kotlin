package com.pubnub.api.models.consumer.objects.uuid

import com.pubnub.api.utils.PatchValue

// TODO add a test that checks sending patch values to server and reading them back when we have the "sending" part
data class PNUUIDMetadata(
    val id: String,
    val name: PatchValue<String?>?,
    val externalId: PatchValue<String?>?,
    val profileUrl: PatchValue<String?>?,
    val email: PatchValue<String?>?,
    val custom: PatchValue<Map<String, Any?>?>?,
    val updated: PatchValue<String>?,
    val eTag: PatchValue<String>?,
    val type: PatchValue<String?>?,
    val status: PatchValue<String?>?,
) {
    /**
     * Merge information from this `PNUUIDMetadata` with new data from `update`, returning a new `PNUUIDMetadata` instance.
     */
    operator fun plus(update: PNUUIDMetadata): PNUUIDMetadata {
        return copy(
            name = update.name ?: name,
            externalId = update.externalId ?: externalId,
            profileUrl = update.profileUrl ?: profileUrl,
            email = update.email ?: email,
            custom = update.custom ?: custom,
            updated = update.updated ?: updated,
            eTag = update.eTag ?: eTag,
            type = update.type ?: type,
            status = update.status ?: status,
        )
    }
}
