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
     * Optional filter expression. Strongly consistent (always reflects the latest writes) but limited in the
     * number of conditionals per request — currently at most 10, which support may raise via keyset
     * configuration. A {@code filter} with more conditionals than allowed is rejected with an error; use
     * {@link #filterAdvanced(String)} for larger or more complex queries.
     */
    GetEntities filter(@Nullable String filter);

    /**
     * Optional advanced filter expression. Same syntax as {@link #filter(String)} but without the
     * conditional-count limit, at the cost of consistency: {@code filterAdvanced} is eventually consistent
     * (recent writes may not yet be reflected), whereas {@link #filter(String)} is strongly consistent.
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