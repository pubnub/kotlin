package com.pubnub.api.java.endpoints.datasync.entity;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncUpdateEntityResult;
import org.jetbrains.annotations.Nullable;

/**
 * @see com.pubnub.api.java.datasync.DataSync#updateEntity(String, List)
 */
public interface UpdateEntity extends Endpoint<PNDataSyncUpdateEntityResult> {
    /**
     * Optional eTag for optimistic concurrency ({@code If-Match} header).
     */
    UpdateEntity ifMatch(@Nullable String ifMatch);
}