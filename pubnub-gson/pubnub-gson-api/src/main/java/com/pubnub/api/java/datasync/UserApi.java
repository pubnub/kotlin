package com.pubnub.api.java.datasync;

import com.pubnub.api.java.endpoints.datasync.user.CreateUser;
import com.pubnub.api.java.endpoints.datasync.user.GetUser;
import com.pubnub.api.java.endpoints.datasync.user.GetUsers;
import com.pubnub.api.java.endpoints.datasync.user.PatchUser;
import com.pubnub.api.java.endpoints.datasync.user.RemoveUser;
import com.pubnub.api.java.endpoints.datasync.user.UpdateUser;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;

import java.util.List;

/**
 * Nested accessor for DataSync User operations, reached via {@link DataSync#user()}.
 *
 * <p>A User is a specialized DataSync entity of class {@code User}.
 */
public interface UserApi {
    /**
     * Get a DataSync user by its id.
     *
     * @param userId Identifier of the user to fetch.
     */
    GetUser get(String userId);

    /**
     * Create a DataSync user. Optional fields are set via the returned builder.
     *
     * @param entityClassVersion Version of the entity class.
     */
    CreateUser create(int entityClassVersion);

    /**
     * Delete a DataSync user by its id.
     *
     * @param userId Identifier of the user to delete.
     */
    RemoveUser delete(String userId);

    /**
     * List DataSync users. Optional filters/paging are set via the returned builder.
     */
    GetUsers getAll();

    /**
     * Partially update a DataSync user via JSON Patch (RFC-6902). Optional {@code ifMatch} is set via the returned builder.
     *
     * @param userId     Identifier of the user to patch.
     * @param operations Non-empty list of JSON Patch operations to apply.
     */
    PatchUser patch(String userId, List<PNJsonPatchOperation> operations);

    /**
     * Fully replace a DataSync user. Optional fields are set via the returned builder.
     * {@code entityClass} is immutable and cannot be updated.
     *
     * @param userId             Identifier of the user to update.
     * @param entityClassVersion Version of the entity class.
     */
    UpdateUser update(String userId, int entityClassVersion);
}
