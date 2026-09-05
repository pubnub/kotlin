package com.pubnub.api.java.endpoints.datasync.membership;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncSetMembershipResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @see com.pubnub.api.java.datasync.DataSync#setMembership(String, int)
 */
public interface SetMembership extends Endpoint<PNDataSyncSetMembershipResult> {
    /**
     * Optional status to store with the membership. Because {@code setMembership} replaces the membership in
     * full, leaving this unset clears any previously stored status rather than preserving it.
     */
    SetMembership status(@Nullable String status);

    /**
     * Optional replacement payload (arbitrary JSON object). Because {@code setMembership} replaces the
     * membership in full, leaving this unset clears any previously stored payload rather than preserving it.
     */
    SetMembership payload(@Nullable Map<String, Object> payload);

    /**
     * Optional eTag for optimistic concurrency ({@code If-Match} header).
     */
    SetMembership ifMatch(@Nullable String ifMatch);
}