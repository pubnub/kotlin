package com.pubnub.api.java.endpoints.datasync.entity;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.entity.PNSetEntityResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @see com.pubnub.api.java.datasync.DataSync#setEntity(String, int)
 */
public interface SetEntity extends Endpoint<PNSetEntityResult> {
    /**
     * Optional entity status.
     */
    SetEntity status(@Nullable String status);

    /**
     * Optional arbitrary JSON object payload.
     */
    SetEntity payload(@Nullable Map<String, Object> payload);

    /**
     * Optional eTag for optimistic concurrency ({@code If-Match} header).
     */
    SetEntity ifMatch(@Nullable String ifMatch);
}
