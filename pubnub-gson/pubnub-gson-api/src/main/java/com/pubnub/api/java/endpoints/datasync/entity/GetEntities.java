package com.pubnub.api.java.endpoints.datasync.entity;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.entity.PNGetEntitiesResult;
import org.jetbrains.annotations.Nullable;

/**
 * @see com.pubnub.api.java.datasync.DataSync#getEntities(String)
 */
public interface GetEntities extends Endpoint<PNGetEntitiesResult> {
    /**
     * Optional entity class version. When not set the server uses the latest.
     */
    GetEntities entityClassVersion(@Nullable Integer entityClassVersion);

    /**
     * Optional level at which the entity class is defined (e.g. {@code SubKey} / {@code Global}).
     */
    GetEntities entityClassLevel(@Nullable String entityClassLevel);

    /**
     * Optional filter expression.
     */
    GetEntities filter(@Nullable String filter);

    /**
     * Optional advanced filter expression.
     */
    GetEntities filterAdvanced(@Nullable String filterAdvanced);

    /**
     * Optional comma-separated sort fields, each optionally suffixed with a direction
     * ({@code :asc} or {@code :desc}, default {@code :asc}), e.g. {@code username:desc,email:asc}.
     */
    GetEntities sort(@Nullable String sort);

    /**
     * Optional page size (1–100, server default 20).
     */
    GetEntities limit(@Nullable Integer limit);

    /**
     * Optional opaque cursor for pagination (from a previous result's {@code next}).
     */
    GetEntities cursor(@Nullable String cursor);
}