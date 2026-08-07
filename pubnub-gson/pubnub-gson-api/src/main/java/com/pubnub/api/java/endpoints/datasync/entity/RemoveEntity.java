package com.pubnub.api.java.endpoints.datasync.entity;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.entity.PNRemoveEntityResult;
import org.jetbrains.annotations.Nullable;

/**
 * @see com.pubnub.api.java.datasync.DataSync#removeEntity(String)
 */
public interface RemoveEntity extends Endpoint<PNRemoveEntityResult> {
    /**
     * Optional eTag for a conditional delete ({@code If-Match} header).
     */
    RemoveEntity ifMatch(@Nullable String ifMatch);
}
