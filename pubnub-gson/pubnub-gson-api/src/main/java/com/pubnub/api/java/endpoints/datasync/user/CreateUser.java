 package com.pubnub.api.java.endpoints.datasync.user;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.user.PNCreateUserResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @see com.pubnub.api.java.datasync.UserApi#create(int)
 */
public interface CreateUser extends Endpoint<PNCreateUserResult> {
    /**
     * Optional user identifier. When not set the server generates one.
     */
    CreateUser userId(@Nullable String userId);

    /**
     * Optional entity class identifier. When not set the server defaults it to {@code User}.
     * When set it must be a {@code User} subclass.
     */
    CreateUser entityClass(@Nullable String entityClass);

    /**
     * Optional user status.
     */
    CreateUser status(@Nullable String status);

    /**
     * Optional arbitrary JSON object payload.
     */
    CreateUser payload(@Nullable Map<String, Object> payload);
}
