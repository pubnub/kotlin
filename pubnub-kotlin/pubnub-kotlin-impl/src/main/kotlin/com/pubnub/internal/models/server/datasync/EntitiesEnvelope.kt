package com.pubnub.internal.models.server.datasync

import com.google.gson.annotations.SerializedName

/**
 * List envelope for DataSync `getEntities`. Unlike [com.pubnub.internal.models.server.objects_api.EntityEnvelope]
 * (single entity, `{ status, data }`), this carries a `data` array plus cursor-based pagination `meta` / `links`.
 */
data class EntitiesEnvelope<T>(
    val status: Int = 0,
    val data: List<T> = emptyList(),
    val meta: EntitiesMeta? = null,
    val links: Map<String, String?>? = null,
)

data class EntitiesMeta(
    @SerializedName("next_cursor") val nextCursor: String? = null,
    @SerializedName("has_next") val hasNext: Boolean = false,
    val limit: Int? = null,
)
