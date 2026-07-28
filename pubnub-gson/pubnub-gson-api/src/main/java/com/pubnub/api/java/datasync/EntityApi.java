package com.pubnub.api.java.datasync;

import com.pubnub.api.java.endpoints.datasync.entity.CreateEntity;
import com.pubnub.api.java.endpoints.datasync.entity.GetEntities;
import com.pubnub.api.java.endpoints.datasync.entity.GetEntity;
import com.pubnub.api.java.endpoints.datasync.entity.PatchEntity;
import com.pubnub.api.java.endpoints.datasync.entity.RemoveEntity;
import com.pubnub.api.java.endpoints.datasync.entity.UpdateEntity;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;

import java.util.List;

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

    /**
     * List DataSync entities of a class. Optional filters/paging are set via the returned builder.
     *
     * @param entityClass Entity class identifier (required).
     */
    GetEntities getAll(String entityClass);

    /**
     * Partially update a DataSync entity via JSON Patch (RFC-6902). Optional {@code ifMatch} is set via the returned builder.
     *
     * @param entityId   Identifier of the entity to patch.
     * @param operations Non-empty list of JSON Patch operations to apply.
     */
    PatchEntity patch(String entityId, List<PNJsonPatchOperation> operations);

    /**
     * Fully replace a DataSync entity. Optional fields are set via the returned builder.
     * {@code entityClass} is immutable and cannot be updated.
     *
     * @param entityId           Identifier of the entity to update.
     * @param entityClassVersion Version of the entity class.
     */
    UpdateEntity update(String entityId, int entityClassVersion);
}
