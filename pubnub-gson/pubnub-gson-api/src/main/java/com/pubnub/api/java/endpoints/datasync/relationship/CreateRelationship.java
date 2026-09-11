package com.pubnub.api.java.endpoints.datasync.relationship;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncCreateRelationshipResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @see com.pubnub.api.java.datasync.DataSync#createRelationship(String, String, String, int)
 */
public interface CreateRelationship extends Endpoint<PNDataSyncCreateRelationshipResult> {
    /**
     * Optional relationship identifier. When not set the server generates one.
     */
    CreateRelationship relationshipId(@Nullable String relationshipId);

    /**
     * Optional relationship status.
     */
    CreateRelationship status(@Nullable String status);

    /**
     * Optional arbitrary JSON object payload.
     */
    CreateRelationship payload(@Nullable Map<String, Object> payload);
}