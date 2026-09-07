package com.pubnub.api.java.endpoints.datasync.membership;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncCreateMembershipResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @see com.pubnub.api.java.datasync.DataSync#createMembership(String, String, int)
 */
public interface CreateMembership extends Endpoint<PNDataSyncCreateMembershipResult> {
    /**
     * Optional membership identifier. When not set the server generates one.
     */
    CreateMembership membershipId(@Nullable String membershipId);

    /**
     * Optional membership status.
     */
    CreateMembership status(@Nullable String status);

    /**
     * Optional arbitrary JSON object payload.
     */
    CreateMembership payload(@Nullable Map<String, Object> payload);
}