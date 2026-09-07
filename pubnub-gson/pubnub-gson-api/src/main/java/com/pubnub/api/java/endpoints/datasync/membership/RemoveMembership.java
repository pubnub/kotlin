package com.pubnub.api.java.endpoints.datasync.membership;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncRemoveMembershipResult;
import org.jetbrains.annotations.Nullable;

/**
 * @see com.pubnub.api.java.datasync.DataSync#removeMembership(String)
 */
public interface RemoveMembership extends Endpoint<PNDataSyncRemoveMembershipResult> {
    /**
     * Optional eTag for a conditional delete ({@code If-Match} header).
     */
    RemoveMembership ifMatch(@Nullable String ifMatch);
}