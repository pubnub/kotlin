package com.pubnub.api.java.endpoints.datasync.entity;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncClassLevel;
import com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncCreateEntityResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @see com.pubnub.api.java.datasync.DataSync#createEntity(String, int)
 */
public interface CreateEntity extends Endpoint<PNDataSyncCreateEntityResult> {
    /**
     * Optional entity identifier. When not set the server generates one.
     */
    CreateEntity entityId(@Nullable String entityId);

    /**
     * Optional level at which the entity class is defined. Disambiguates a class defined at more than
     * one level. Create-only — not accepted by {@code setEntity}.
     */
    CreateEntity classLevel(@Nullable PNDataSyncClassLevel classLevel);

    /**
     * Optional entity status.
     */
    CreateEntity status(@Nullable String status);

    /**
     * Optional arbitrary JSON object payload.
     */
    CreateEntity payload(@Nullable Map<String, Object> payload);
}
