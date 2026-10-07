package com.pubnub.api.java.endpoints.datasync.relationship;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncSetRelationshipResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @see com.pubnub.api.java.datasync.DataSync#setRelationship(String, int)
 */
public interface SetRelationship extends Endpoint<PNDataSyncSetRelationshipResult> {
    /**
     * Optional status to store with the relationship. Because {@code setRelationship} replaces the relationship
     * in full, leaving this unset clears any previously stored status rather than preserving it.
     */
    SetRelationship status(@Nullable String status);

    /**
     * Optional replacement payload (arbitrary JSON object). Because {@code setRelationship} replaces the
     * relationship in full, leaving this unset clears any previously stored payload rather than preserving it.
     */
    SetRelationship payload(@Nullable Map<String, Object> payload);

    /**
     * Optional eTag for optimistic concurrency ({@code If-Match} header).
     */
    SetRelationship ifMatch(@Nullable String ifMatch);
}
