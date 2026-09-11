package com.pubnub.api.models.consumer.datasync.relationship

import com.pubnub.api.utils.SerializedName

/**
 * DataSync Relationship resource returned by `pubnub.dataSync.getRelationship`, `createRelationship` and
 * referenced by remove operations. A Relationship is a typed link between two entities (side A and side B) under
 * an arbitrary relationship class, exposed under the generic two-end names [entityAId] / [entityBId].
 *
 * @property id Relationship identifier.
 * @property entityAId Identifier of entity A this relationship links.
 * @property entityBId Identifier of entity B this relationship links.
 * @property className Relationship class identifier.
 * @property classVersion Version of the relationship class.
 * @property createdAt Date and time the relationship was created.
 * @property updatedAt Date and time the relationship was last updated.
 * @property eTag The relationship's content fingerprint used in conditional requests.
 * @property expiresAt Date and time when the relationship expires (will be deleted automatically). Always
 * present: server-computed on every response, never client-settable.
 * @property status Relationship status.
 * @property payload Arbitrary user-defined JSON object.
 */
data class PNDataSyncRelationship(
    val id: String,
    val entityAId: String,
    val entityBId: String,
    @field:SerializedName("relationshipClass") val className: String,
    @field:SerializedName("relationshipClassVersion") val classVersion: Int,
    val createdAt: String,
    val updatedAt: String,
    val eTag: String,
    val expiresAt: String,
    val status: String? = null,
    val payload: Map<String, Any?>? = null,
)
