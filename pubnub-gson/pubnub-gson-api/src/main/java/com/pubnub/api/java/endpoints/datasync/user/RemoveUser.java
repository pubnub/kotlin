package com.pubnub.api.java.endpoints.datasync.user;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.user.DataSyncRemoveUserResult;
import org.jetbrains.annotations.Nullable;

/**
 * @see com.pubnub.api.java.datasync.DataSync#removeUser(String)
 */
public interface RemoveUser extends Endpoint<DataSyncRemoveUserResult> {
    /**
     * Optional eTag for a conditional delete ({@code If-Match} header).
     */
    RemoveUser ifMatch(@Nullable String ifMatch);
}
