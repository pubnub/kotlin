package com.pubnub.api.models.consumer.datasync.channel

import com.pubnub.api.utils.SerializedName

/**
 * DataSync Channel resource returned by `pubnub.dataSync.getChannel`, `createChannel` and referenced by
 * remove operations. A Channel is a specialized DataSync entity of class `Channel`.
 *
 * @property id Channel identifier.
 * @property className Entity class identifier (a `Channel` subclass, defaults to `Channel`).
 * @property classVersion Version of the entity class.
 * @property classLevel The level at which the entity class is defined (e.g. `SubKey` / `Global`).
 *   Modeled as a nullable [String] rather than an enum to stay source-compatible with future values.
 * @property createdAt Date and time the channel was created.
 * @property updatedAt Date and time the channel was last updated.
 * @property eTag The channel's content fingerprint used in conditional requests.
 * @property expiresAt Date and time when the channel expires (will be deleted automatically).
 *   Always present — server-computed from the entity class TTL and never client-settable.
 * @property status Channel status.
 * @property payload Arbitrary user-defined JSON object.
 */
data class PNDataSyncChannel(
    val id: String,
    @field:SerializedName("entityClass") val className: String,
    @field:SerializedName("entityClassVersion") val classVersion: Int,
    @field:SerializedName("entityClassLevel") val classLevel: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val eTag: String,
    val expiresAt: String,
    val status: String? = null,
    val payload: Map<String, Any?>? = null,
)
