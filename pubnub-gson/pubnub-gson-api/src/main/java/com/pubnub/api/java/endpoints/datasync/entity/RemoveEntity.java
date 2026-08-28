package com.pubnub.api.java.endpoints.datasync.entity;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.entity.DataSyncRemoveEntityResult;
import org.jetbrains.annotations.Nullable;

/**
 * @see com.pubnub.api.java.datasync.DataSync#removeEntity(String)
 */
public interface RemoveEntity extends Endpoint<DataSyncRemoveEntityResult> {
    /**
     * Optional eTag for a conditional delete ({@code If-Match} header).
     */
    RemoveEntity ifMatch(@Nullable String ifMatch);
}
