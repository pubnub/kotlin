package com.pubnub.api.java.endpoints.datasync.user;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncClassLevel;
import com.pubnub.api.java.models.consumer.datasync.user.PNDataSyncCreateUserResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @see com.pubnub.api.java.datasync.DataSync#createUser(int)
 */
public interface CreateUser extends Endpoint<PNDataSyncCreateUserResult> {
    /**
     * Optional user identifier. When not set the server generates one.
     */
    CreateUser userId(@Nullable String userId);

    /**
     * Optional entity class identifier. When not set the server defaults it to {@code User}.
     * When set it must be a {@code User} subclass.
     */
    CreateUser className(@Nullable String className);

    /**
     * Optional level at which the entity class is defined. Disambiguates a class defined at more than
     * one level. Create-only — not accepted by {@code setUser}. The built-in {@code User} class is
     * defined at the {@code GLOBAL} level.
     */
    CreateUser classLevel(@Nullable PNDataSyncClassLevel classLevel);

    /**
     * Optional user status.
     */
    CreateUser status(@Nullable String status);

    /**
     * Optional arbitrary JSON object payload. The payload is free-form, but the built-in {@code User} class
     * indexes the {@code name} and {@code type} properties: populating them here makes the user filterable and
     * sortable on {@code name} / {@code type} in {@code getUsers}. Any other payload property is stored but not
     * queryable unless a subclass that extends {@code User} declares it filterable (such a subclass inherits the
     * {@code name} / {@code type} indexes and may add its own). The built-in fields {@code id}, {@code createdAt},
     * {@code updatedAt}, and {@code status} are always filterable/sortable regardless of payload.
     */
    CreateUser payload(@Nullable Map<String, Object> payload);
}
