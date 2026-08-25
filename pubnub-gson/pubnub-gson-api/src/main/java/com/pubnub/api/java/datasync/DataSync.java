package com.pubnub.api.java.datasync;

import com.pubnub.api.java.endpoints.datasync.entity.CreateEntity;
import com.pubnub.api.java.endpoints.datasync.entity.GetEntities;
import com.pubnub.api.java.endpoints.datasync.entity.GetEntity;
import com.pubnub.api.java.endpoints.datasync.entity.UpdateEntity;
import com.pubnub.api.java.endpoints.datasync.entity.RemoveEntity;
import com.pubnub.api.java.endpoints.datasync.entity.SetEntity;
import com.pubnub.api.java.endpoints.datasync.user.CreateUser;
import com.pubnub.api.java.endpoints.datasync.user.GetUser;
import com.pubnub.api.java.endpoints.datasync.user.GetUsers;
import com.pubnub.api.java.endpoints.datasync.user.UpdateUser;
import com.pubnub.api.java.endpoints.datasync.user.RemoveUser;
import com.pubnub.api.java.endpoints.datasync.user.SetUser;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;

import java.util.List;

/**
 * Entry point for the DataSync API, reached via {@code pubnub.dataSync()}.
 */
public interface DataSync {
    /**
     * Get a DataSync entity by its id.
     *
     * @param entityId Identifier of the entity to fetch.
     */
    GetEntity getEntity(String entityId);

    /**
     * Create a DataSync entity. Optional fields are set via the returned builder.
     *
     * @param entityClass        Entity class identifier.
     * @param entityClassVersion Version of the entity class.
     */
    CreateEntity createEntity(String entityClass, int entityClassVersion);

    /**
     * Remove a DataSync entity by its id.
     *
     * @param entityId Identifier of the entity to remove.
     */
    RemoveEntity removeEntity(String entityId);

    /**
     * List DataSync entities of a class. Optional filters/paging are set via the returned builder.
     *
     * @param entityClass Entity class identifier (required).
     */
    GetEntities getEntities(String entityClass);

    /**
     * Partially update a DataSync entity via JSON Patch (RFC-6902). Optional {@code ifMatch} is set via the returned builder.
     *
     * @param entityId   Identifier of the entity to patch.
     * @param operations Non-empty list of JSON Patch operations to apply.
     */
    UpdateEntity updateEntity(String entityId, List<PNJsonPatchOperation> operations);

    /**
     * Fully replace a DataSync entity. Optional fields are set via the returned builder.
     * {@code entityClass} is immutable and cannot be updated.
     *
     * @param entityId           Identifier of the entity to update.
     * @param entityClassVersion Version of the entity class.
     */
    SetEntity setEntity(String entityId, int entityClassVersion);

    /**
     * Get a DataSync user by its id.
     *
     * <p>A User is a specialized DataSync entity of class {@code User}.
     *
     * @param userId Identifier of the user to fetch.
     */
    GetUser getUser(String userId);

    /**
     * Create a DataSync user. Optional fields are set via the returned builder.
     *
     * @param entityClassVersion Version of the entity class.
     */
    CreateUser createUser(int entityClassVersion);

    /**
     * Remove a DataSync user by its id.
     *
     * @param userId Identifier of the user to remove.
     */
    RemoveUser removeUser(String userId);

    /**
     * List DataSync users. Optional filters/paging are set via the returned builder.
     */
    GetUsers getUsers();

    /**
     * Partially update a DataSync user via JSON Patch (RFC-6902). Optional {@code ifMatch} is set via the returned builder.
     *
     * @param userId     Identifier of the user to patch.
     * @param operations Non-empty list of JSON Patch operations to apply.
     */
    UpdateUser updateUser(String userId, List<PNJsonPatchOperation> operations);

    /**
     * Fully replace a DataSync user. Optional fields are set via the returned builder.
     * {@code entityClass} is immutable and cannot be updated.
     *
     * @param userId             Identifier of the user to update.
     * @param entityClassVersion Version of the entity class.
     */
    SetUser setUser(String userId, int entityClassVersion);
}