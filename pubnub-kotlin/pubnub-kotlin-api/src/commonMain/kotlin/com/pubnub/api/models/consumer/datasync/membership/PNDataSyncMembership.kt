package com.pubnub.api.models.consumer.datasync.membership

import com.pubnub.api.utils.SerializedName

/**
 * DataSync Membership resource returned by `pubnub.dataSync.getMembership`, `createMembership` and referenced
 * by remove operations. A Membership is a specialized DataSync relationship linking a Channel (entity A) and a
 * User (entity B), re-exposed under the domain-friendly names [channelId] / [userId].
 *
 * @property id Membership identifier.
 * @property channelId Identifier of the Channel (entity A) this membership links.
 * @property userId Identifier of the User (entity B) this membership links.
 * @property className Relationship class identifier (always `Membership`).
 * @property classVersion Version of the relationship class.
 * @property createdAt Date and time the membership was created.
 * @property updatedAt Date and time the membership was last updated.
 * @property eTag The membership's content fingerprint used in conditional requests.
 * @property expiresAt Date and time when the membership expires (will be deleted automatically). Always
 * present: server-computed on every response, never client-settable.
 * @property status Membership status.
 * @property payload Arbitrary user-defined JSON object.
 */
data class PNDataSyncMembership(
    val id: String,
    val channelId: String,
    val userId: String,
    @field:SerializedName("relationshipClass") val className: String,
    @field:SerializedName("relationshipClassVersion") val classVersion: Int,
    val createdAt: String,
    val updatedAt: String,
    val eTag: String,
    val expiresAt: String,
    val status: String? = null,
    val payload: Map<String, Any?>? = null,
)
