package com.pubnub.api.java.endpoints.datasync.user;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.user.PNUpdateUserResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @see com.pubnub.api.java.datasync.DataSync#updateUser(String, int)
 */
public interface UpdateUser extends Endpoint<PNUpdateUserResult> {
    /**
     * Optional user status.
     */
    UpdateUser status(@Nullable String status);

    /**
     * Optional arbitrary JSON object payload.
     */
    UpdateUser payload(@Nullable Map<String, Object> payload);

    /**
     * Optional eTag for optimistic concurrency ({@code If-Match} header).
     */
    UpdateUser ifMatch(@Nullable String ifMatch);
}
