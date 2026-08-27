package com.pubnub.api.models.consumer.datasync.channel

import com.pubnub.api.utils.SerializedName

/**
 * DataSync Channel resource returned by `pubnub.dataSync.getChannel`, `createChannel` and referenced by
 * remove operations. A Channel is a specialized DataSync entity of class `Channel`.
 *
 * The class-identity properties are exposed with the `class*` names (`className` / `classVersion` /
 * `classLevel`), while the server serializes them under the camelCase `entityClass*` wire keys — hence the
 * `@field:SerializedName` mappings below.
 *
 * @property id Channel identifier.
 * @property className Entity class identifier (a `Channel` subclass, defaults to `Channel`).
 * @property classVersion Version of the entity class.
 * @property classLevel The level at which the entity class is defined (e.g. `SubKey` / `Global`).
 *   Modeled as a nullable [String] rather than an enum to stay source-compatible with future values.
 * @property createdAt Date and time the channel was created.
 * @property updatedAt Date and time the channel was last updated.
 * @property eTag The channel's content fingerprint used in conditional requests.
 * @property status Channel status.
 * @property expiresAt Date and time when the channel expires (will be deleted automatically).
 * @property payload Arbitrary user-defined JSON object.
 */
data class DataSyncChannel(
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
