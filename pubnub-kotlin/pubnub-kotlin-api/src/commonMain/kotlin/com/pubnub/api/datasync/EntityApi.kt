package com.pubnub.api.datasync

import com.pubnub.api.endpoints.datasync.entity.CreateEntity
import com.pubnub.api.endpoints.datasync.entity.GetEntities
import com.pubnub.api.endpoints.datasync.entity.GetEntity
import com.pubnub.api.endpoints.datasync.entity.PatchEntity
import com.pubnub.api.endpoints.datasync.entity.RemoveEntity
import com.pubnub.api.endpoints.datasync.entity.UpdateEntity
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation

/**
 * Nested accessor for DataSync Entity operations, reached via [DataSync.entity].
 */
interface EntityApi {
    /**
     * Get a DataSync entity by its id.
     *
     * @param entityId Identifier of the entity to fetch.
     */
    fun get(entityId: String): GetEntity

    /**
     * Create a DataSync entity.
     *
     * @param entityClass Entity class identifier.
     * @param entityClassVersion Version of the entity class.
     * @param entityId Optional entity identifier. When `null` the server generates one.
     * @param status Optional entity status.
     * @param payload Optional arbitrary JSON object payload.
     */
    fun create(
        entityClass: String,
        entityClassVersion: Int,
        entityId: String? = null,
        status: String? = null,
        payload: Any? = null,
    ): CreateEntity

    /**
     * Delete a DataSync entity by its id.
     *
     * @param entityId Identifier of the entity to delete.
     * @param ifMatch Optional eTag for a conditional delete (`If-Match` header).
     */
    fun delete(entityId: String, ifMatch: String? = null): RemoveEntity

    /**
     * List DataSync entities of a class.
     *
     * @param entityClass Entity class identifier (required).
     * @param entityClassVersion Optional entity class version. When `null` the server uses the latest.
     * @param filter Optional filter expression.
     * @param filterAdvanced Optional advanced filter expression.
     * @param sort Optional comma-separated sort fields, each optionally suffixed with a direction
     *   (`:asc` or `:desc`, default `:asc`), e.g. `username:desc,email:asc`.
     * @param limit Optional page size (1–100, server default 20).
     * @param cursor Optional opaque cursor for pagination (from a previous result's `next`).
     */
    fun getAll(
        entityClass: String,
        entityClassVersion: Int? = null,
        filter: String? = null,
        filterAdvanced: String? = null,
        sort: String? = null,
        limit: Int? = null,
        cursor: String? = null,
    ): GetEntities

    /**
     * Partially update a DataSync entity via JSON Patch (RFC-6902).
     *
     * @param entityId Identifier of the entity to patch.
     * @param operations Non-empty list of JSON Patch operations to apply.
     * @param ifMatch Optional eTag for optimistic concurrency (`If-Match` header).
     */
    fun patch(
        entityId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String? = null,
    ): PatchEntity

    /**
     * Fully replace a DataSync entity. `entityClass` is immutable and cannot be updated.
     *
     * @param entityId Identifier of the entity to update.
     * @param entityClassVersion Version of the entity class.
     * @param status Optional entity status.
     * @param payload Optional arbitrary JSON object payload.
     * @param ifMatch Optional eTag for optimistic concurrency (`If-Match` header).
     */
    fun update(
        entityId: String,
        entityClassVersion: Int,
        status: String? = null,
        payload: Any? = null,
        ifMatch: String? = null,
    ): UpdateEntity
}
