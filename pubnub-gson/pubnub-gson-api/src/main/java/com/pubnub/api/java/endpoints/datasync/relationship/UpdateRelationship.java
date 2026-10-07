package com.pubnub.api.java.endpoints.datasync.relationship;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncUpdateRelationshipResult;
import org.jetbrains.annotations.Nullable;

/**
 * @see com.pubnub.api.java.datasync.DataSync#updateRelationship(String, java.util.List)
 */
public interface UpdateRelationship extends Endpoint<PNDataSyncUpdateRelationshipResult> {
    /**
     * Optional eTag for optimistic concurrency ({@code If-Match} header).
     */
    UpdateRelationship ifMatch(@Nullable String ifMatch);
}
