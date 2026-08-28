package com.pubnub.api.models.consumer.datasync.user

import com.pubnub.api.utils.SerializedName

/**
 * DataSync User resource returned by `pubnub.dataSync.getUser`, `createUser` and referenced by
 * remove operations. A User is a specialized DataSync entity of class `User`.
 *
 * The class-identity properties are exposed with the `class*` names (`className` / `classVersion` /
 * `classLevel`), while the server serializes them under the camelCase `entityClass*` wire keys — hence the
 * `@field:SerializedName` mappings below.
 *
 * @property id User identifier.
 * @property className Entity class identifier (a `User` subclass, defaults to `User`).
 * @property classVersion Version of the entity class.
 * @property classLevel The level at which the entity class is defined (e.g. `SubKey` / `Global`).
 *   Modeled as a nullable [String] rather than an enum to stay source-compatible with future values.
 * @property createdAt Date and time the user was created.
 * @property updatedAt Date and time the user was last updated.
 * @property eTag The user's content fingerprint used in conditional requests.
 * @property status User status.
 * @property expiresAt Date and time when the user expires (will be deleted automatically).
 * @property payload Arbitrary user-defined JSON object.
 */
data class DataSyncUser(
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
