package com.pubnub.api.java.endpoints.datasync.user;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.user.DataSyncUpdateUserResult;
import org.jetbrains.annotations.Nullable;

/**
 * @see com.pubnub.api.java.datasync.DataSync#updateUser(String, java.util.List)
 */
public interface UpdateUser extends Endpoint<DataSyncUpdateUserResult> {
    /**
     * Optional eTag for optimistic concurrency ({@code If-Match} header).
     */
    UpdateUser ifMatch(@Nullable String ifMatch);
}
