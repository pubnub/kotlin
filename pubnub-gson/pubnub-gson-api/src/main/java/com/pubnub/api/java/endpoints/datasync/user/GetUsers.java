package com.pubnub.api.java.endpoints.datasync.user;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncClassLevel;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncSortField;
import com.pubnub.api.java.models.consumer.datasync.user.PNDataSyncGetUsersResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @see com.pubnub.api.java.datasync.DataSync#getUsers()
 */
public interface GetUsers extends Endpoint<PNDataSyncGetUsersResult> {
    /**
     * Optional entity class identifier. When not set the whole User family is returned;
     * when set it narrows the results to that {@code User} subclass.
     */
    GetUsers className(@Nullable String className);

    /**
     * Optional entity class version. When not set the server uses the latest.
     */
    GetUsers classVersion(@Nullable Integer classVersion);

    /**
     * Optional level at which the entity class is defined. The built-in {@code User} class is defined at
     * the {@code GLOBAL} level.
     */
    GetUsers classLevel(@Nullable PNDataSyncClassLevel classLevel);

    /**
     * Optional filter expression. Filtering is only allowed on the entity class's properties whose filtering
     * mode is not disabled (i.e. those the class marks as filterable); filtering on any other property
     * returns a server error. For the built-in {@code User} class this set is {@code name} ({@code username}
     * and {@code email} are custom-class properties, not built-in {@code User} fields).
     *
     * <p>{@code filter} is strongly consistent (it always reflects the latest writes) but limited in the number
     * of conditionals per request — currently at most 10, which support may raise via keyset configuration. A
     * {@code filter} with more conditionals than allowed is rejected with an error; use
     * {@link #filterAdvanced(String)} for larger or more complex queries.
     */
    GetUsers filter(@Nullable String filter);

    /**
     * Optional advanced filter expression. Uses the same syntax and filterable-property rules as
     * {@link #filter(String)} but is not subject to the conditional-count limit, so use it for larger or more
     * complex queries. The trade-off is consistency: {@code filterAdvanced} is eventually consistent (recent
     * writes may not yet be reflected), whereas {@link #filter(String)} is strongly consistent.
     */
    GetUsers filterAdvanced(@Nullable String filterAdvanced);

    /**
     * Optional sort criteria applied in order; each {@link PNDataSyncSortField} sorts on a payload
     * property either ascending (default) or descending. Sorting is governed by the same filterable-property
     * rule as {@link #filter(String)} (built-in {@code User} class: {@code name}).
     */
    GetUsers sort(@Nullable List<PNDataSyncSortField> sort);

    /**
     * Optional page size (1–100, server default 20).
     */
    GetUsers limit(@Nullable Integer limit);

    /**
     * Optional opaque cursor for pagination (from a previous result's {@code next.getCursor()}).
     */
    GetUsers cursor(@Nullable String cursor);
}
