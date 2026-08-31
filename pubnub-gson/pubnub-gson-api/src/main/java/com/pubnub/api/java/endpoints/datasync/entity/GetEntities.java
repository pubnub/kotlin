package com.pubnub.api.java.endpoints.datasync.entity;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncClassLevel;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncSortField;
import com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncGetEntitiesResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @see com.pubnub.api.java.datasync.DataSync#getEntities(String)
 */
public interface GetEntities extends Endpoint<PNDataSyncGetEntitiesResult> {
    /**
     * Optional entity class version. When not set the server uses the latest.
     */
    GetEntities classVersion(@Nullable Integer classVersion);

    /**
     * Optional level at which the entity class is defined (e.g. {@code SubKey} / {@code Global}).
     */
    GetEntities classLevel(@Nullable PNDataSyncClassLevel classLevel);

    /**
     * Optional filter expression. Filtering is only allowed on the entity class's properties whose filtering
     * mode is not disabled (i.e. those the class marks as filterable); filtering on any other property
     * returns a server error. The filterable set depends on the entity class supplied to
     * {@code getEntities(className)} — an Entity has no built-in default set.
     *
     * <p>{@code filter} is strongly consistent (it always reflects the latest writes) but limited in the number
     * of conditionals per request — currently at most 10, which support may raise via keyset configuration. A
     * {@code filter} with more conditionals than allowed is rejected with an error; use
     * {@link #filterAdvanced(String)} for larger or more complex queries.
     */
    GetEntities filter(@Nullable String filter);

    /**
     * Optional advanced filter expression. Uses the same syntax and filterable-property rules as
     * {@link #filter(String)} but is not subject to the conditional-count limit, so use it for larger or more
     * complex queries. The trade-off is consistency: {@code filterAdvanced} is eventually consistent (recent
     * writes may not yet be reflected), whereas {@link #filter(String)} is strongly consistent.
     */
    GetEntities filterAdvanced(@Nullable String filterAdvanced);

    /**
     * Optional sort criteria applied in order; each {@link PNDataSyncSortField} sorts on a payload
     * property either ascending (default) or descending. Sorting is governed by the same filterable-property
     * rule as {@link #filter(String)} — only properties the entity class marks as filterable may be used.
     */
    GetEntities sort(@Nullable List<PNDataSyncSortField> sort);

    /**
     * Optional page size (1–100, server default 20).
     */
    GetEntities limit(@Nullable Integer limit);

    /**
     * Optional opaque cursor for pagination (from a previous result's {@code next.getCursor()}).
     */
    GetEntities cursor(@Nullable String cursor);
}
