package com.pubnub.api.models.consumer.datasync.entity

/**
 * DataSync Entity resource returned by `pubnub.dataSync.entity.get`, `create` and referenced by
 * remove operations.
 *
 * @property id Entity identifier.
 * @property entityClass Entity class identifier.
 * @property entityClassVersion Version of the entity class.
 * @property entityClassLevel The level at which the entity class is defined (e.g. `SubKey` / `Global`).
 *   Modeled as a nullable [String] rather than an enum to stay source-compatible with future values.
 * @property createdAt Date and time the entity was created.
 * @property updatedAt Date and time the entity was last updated.
 * @property eTag The entity's content fingerprint used in conditional requests.
 * @property status Entity status.
 * @property expiresAt Date and time when the entity expires (will be deleted automatically).
 * @property payload Arbitrary user-defined JSON object.
 */
data class PNEntity(
    val id: String,
    val entityClass: String,
    val entityClassVersion: Int,
    val entityClassLevel: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val eTag: String,
    val status: String? = null,
    val expiresAt: String? = null,
    val payload: Map<String, Any?>? = null,
)
