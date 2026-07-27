package com.pubnub.api.datasync

import com.pubnub.api.endpoints.datasync.entity.CreateEntity
import com.pubnub.api.endpoints.datasync.entity.GetEntity
import com.pubnub.api.endpoints.datasync.entity.RemoveEntity

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
}
