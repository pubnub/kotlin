package com.pubnub.api.java.endpoints.datasync.entity;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.entity.PNUpdateEntityResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @see com.pubnub.api.java.datasync.DataSync#updateEntity(String, int)
 */
public interface UpdateEntity extends Endpoint<PNUpdateEntityResult> {
    /**
     * Optional entity status.
     */
    UpdateEntity status(@Nullable String status);

    /**
     * Optional arbitrary JSON object payload.
     */
    UpdateEntity payload(@Nullable Map<String, Object> payload);

    /**
     * Optional eTag for optimistic concurrency ({@code If-Match} header).
     */
    UpdateEntity ifMatch(@Nullable String ifMatch);
}
