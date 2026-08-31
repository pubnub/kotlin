package com.pubnub.api.models.consumer.datasync.entity

import com.pubnub.api.utils.SerializedName

/**
 * DataSync Entity resource returned by `pubnub.dataSync.getEntity`, `createEntity` and referenced by
 * remove operations.
 *
 * The class-identity properties are exposed with the `class*` names (`className` / `classVersion` /
 * `classLevel`), while the server serializes them under the camelCase `entityClass*` wire keys — hence the
 * `@field:SerializedName` mappings below.
 *
 * @property id Entity identifier.
 * @property className Entity class identifier.
 * @property classVersion Version of the entity class.
 * @property classLevel The level at which the entity class is defined (e.g. `SubKey` / `Global`).
 *   Modeled as a nullable [String] rather than an enum to stay source-compatible with future values.
 * @property createdAt Date and time the entity was created.
 * @property updatedAt Date and time the entity was last updated.
 * @property eTag The entity's content fingerprint used in conditional requests.
 * @property status Entity status.
 * @property expiresAt Date and time when the entity expires (will be deleted automatically).
 * @property payload Arbitrary user-defined JSON object.
 */
data class PNDataSyncEntity(
    val id: String,
    @field:SerializedName("entityClass") val className: String,
    @field:SerializedName("entityClassVersion") val classVersion: Int,
    @field:SerializedName("entityClassLevel") val classLevel: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val eTag: String,
    val status: String? = null,
    val expiresAt: String? = null,
    val payload: Map<String, Any?>? = null,
)
