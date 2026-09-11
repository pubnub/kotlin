package com.pubnub.api.java.endpoints.datasync.relationship;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncRemoveRelationshipResult;
import org.jetbrains.annotations.Nullable;

/**
 * @see com.pubnub.api.java.datasync.DataSync#removeRelationship(String)
 */
public interface RemoveRelationship extends Endpoint<PNDataSyncRemoveRelationshipResult> {
    /**
     * Optional eTag for a conditional delete ({@code If-Match} header).
     */
    RemoveRelationship ifMatch(@Nullable String ifMatch);
}
