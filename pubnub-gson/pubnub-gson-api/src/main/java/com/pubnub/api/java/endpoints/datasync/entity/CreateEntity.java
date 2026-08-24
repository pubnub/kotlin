package com.pubnub.api.java.endpoints.datasync.entity;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.entity.PNCreateEntityResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @see com.pubnub.api.java.datasync.DataSync#createEntity(String, int)
 */
public interface CreateEntity extends Endpoint<PNCreateEntityResult> {
    /**
     * Optional entity identifier. When not set the server generates one.
     */
    CreateEntity entityId(@Nullable String entityId);

    /**
     * Optional entity status.
     */
    CreateEntity status(@Nullable String status);

    /**
     * Optional arbitrary JSON object payload.
     */
    CreateEntity payload(@Nullable Map<String, Object> payload);
}
