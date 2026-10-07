package com.pubnub.api.java.endpoints.datasync.membership;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncUpdateMembershipResult;
import org.jetbrains.annotations.Nullable;

/**
 * @see com.pubnub.api.java.datasync.DataSync#updateMembership(String, java.util.List)
 */
public interface UpdateMembership extends Endpoint<PNDataSyncUpdateMembershipResult> {
    /**
     * Optional eTag for optimistic concurrency ({@code If-Match} header).
     */
    UpdateMembership ifMatch(@Nullable String ifMatch);
}