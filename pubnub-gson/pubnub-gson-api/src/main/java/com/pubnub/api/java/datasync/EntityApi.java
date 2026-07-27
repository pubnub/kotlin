package com.pubnub.api.java.datasync;

import com.pubnub.api.java.endpoints.datasync.entity.CreateEntity;
import com.pubnub.api.java.endpoints.datasync.entity.GetEntity;
import com.pubnub.api.java.endpoints.datasync.entity.RemoveEntity;

/**
 * Nested accessor for DataSync Entity operations, reached via {@link DataSync#entity()}.
 */
public interface EntityApi {
    /**
     * Get a DataSync entity by its id.
     *
     * @param entityId Identifier of the entity to fetch.
     */
    GetEntity get(String entityId);

    /**
     * Create a DataSync entity. Optional fields are set via the returned builder.
     *
     * @param entityClass        Entity class identifier.
     * @param entityClassVersion Version of the entity class.
     */
    CreateEntity create(String entityClass, int entityClassVersion);

    /**
     * Delete a DataSync entity by its id.
     *
     * @param entityId Identifier of the entity to delete.
     */
    RemoveEntity delete(String entityId);
}
