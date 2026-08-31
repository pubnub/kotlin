package com.pubnub.api.java.endpoints.datasync.user;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.user.PNDataSyncSetUserResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @see com.pubnub.api.java.datasync.DataSync#setUser(String, int)
 */
public interface SetUser extends Endpoint<PNDataSyncSetUserResult> {
    /**
     * Optional user status.
     */
    SetUser status(@Nullable String status);

    /**
     * Optional arbitrary JSON object payload.
     */
    SetUser payload(@Nullable Map<String, Object> payload);

    /**
     * Optional eTag for optimistic concurrency ({@code If-Match} header).
     */
    SetUser ifMatch(@Nullable String ifMatch);
}
