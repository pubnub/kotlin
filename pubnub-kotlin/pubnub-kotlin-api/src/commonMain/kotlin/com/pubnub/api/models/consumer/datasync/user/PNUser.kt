package com.pubnub.api.models.consumer.datasync.user

/**
 * DataSync User resource returned by `pubnub.dataSync.user.get`, `create` and referenced by
 * remove operations. A User is a specialized DataSync entity of class `User`.
 *
 * @property id User identifier.
 * @property entityClass Entity class identifier (a `User` subclass, defaults to `User`).
 * @property entityClassVersion Version of the entity class.
 * @property entityClassLevel The level at which the entity class is defined (e.g. `SubKey` / `Global`).
 *   Modeled as a nullable [String] rather than an enum to stay source-compatible with future values.
 * @property createdAt Date and time the user was created.
 * @property updatedAt Date and time the user was last updated.
 * @property eTag The user's content fingerprint used in conditional requests.
 * @property status User status.
 * @property expiresAt Date and time when the user expires (will be deleted automatically).
 * @property payload Arbitrary user-defined JSON object.
 */
data class PNUser(
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
