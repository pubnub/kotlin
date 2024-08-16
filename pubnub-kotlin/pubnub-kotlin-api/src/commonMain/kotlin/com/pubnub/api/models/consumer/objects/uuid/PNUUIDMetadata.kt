package com.pubnub.api.models.consumer.objects.uuid

import com.pubnub.api.utils.PatchValue

// TODO add a test that checks sending patch values to server and reading them back when we have the "sending" part
data class PNUUIDMetadata(
    val id: String,
    val name: PatchValue<String?>? = null,
    val externalId: PatchValue<String?>? = null,
    val profileUrl: PatchValue<String?>? = null,
    val email: PatchValue<String?>? = null,
    val custom: PatchValue<Map<String, Any?>?>? = null,
    val updated: PatchValue<String>? = null,
    val eTag: PatchValue<String>? = null,
    val type: PatchValue<String?>? = null,
    val status: PatchValue<String?>? = null,
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
