package com.pubnub.api.java.endpoints.datasync.user;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.user.PNPatchUserResult;
import org.jetbrains.annotations.Nullable;

/**
 * @see com.pubnub.api.java.datasync.DataSync#patchUser(String, java.util.List)
 */
public interface PatchUser extends Endpoint<PNPatchUserResult> {
    /**
     * Optional eTag for optimistic concurrency ({@code If-Match} header).
     */
    PatchUser ifMatch(@Nullable String ifMatch);
}
