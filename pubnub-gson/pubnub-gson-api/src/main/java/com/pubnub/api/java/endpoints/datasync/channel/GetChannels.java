package com.pubnub.api.java.endpoints.datasync.channel;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncClassLevel;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncSortField;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncGetChannelsResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @see com.pubnub.api.java.datasync.DataSync#getChannels()
 */
public interface GetChannels extends Endpoint<PNDataSyncGetChannelsResult> {
    /**
     * Optional entity class identifier. When not set the whole Channel family is returned;
     * when set it narrows the results to that {@code Channel} subclass.
     */
    GetChannels className(@Nullable String className);

    /**
     * Optional entity class version. When not set the server uses the latest.
     */
    GetChannels classVersion(@Nullable Integer classVersion);

    /**
     * Optional level at which the entity class is defined. The built-in {@code Channel} class is defined at
     * the {@code GLOBAL} level.
     */
    GetChannels classLevel(@Nullable PNDataSyncClassLevel classLevel);

    /**
     * Optional filter expression. Filtering is only allowed on the entity class's properties whose filtering
     * mode is not disabled (i.e. those the class marks as filterable); filtering on any other property
     * returns a server error. For the default {@code Channel} class this set is {@code name} and {@code type},
     * but a custom class or subclass may declare additional filterable properties.
     *
     * <p>{@code filter} is strongly consistent (it always reflects the latest writes) but limited in the number
     * of conditionals per request — currently at most 10, which support may raise via keyset configuration. A
     * {@code filter} with more conditionals than allowed is rejected with an error; use
     * {@link #filterAdvanced(String)} for larger or more complex queries.
     */
    GetChannels filter(@Nullable String filter);

    /**
     * Optional advanced filter expression. Uses the same syntax and filterable-property rules as
     * {@link #filter(String)} but is not subject to the conditional-count limit, so use it for larger or more
     * complex queries. The trade-off is consistency: {@code filterAdvanced} is eventually consistent (recent
     * writes may not yet be reflected), whereas {@link #filter(String)} is strongly consistent.
     */
    GetChannels filterAdvanced(@Nullable String filterAdvanced);

    /**
     * Optional sort criteria applied in order; each {@link PNDataSyncSortField} sorts on a payload
     * property either ascending (default) or descending. Sorting is governed by the same filterable-property
     * rule as {@link #filter(String)} (default {@code Channel} class: {@code name} and {@code type}).
     */
    GetChannels sort(@Nullable List<PNDataSyncSortField> sort);

    /**
     * Optional page size (1–100, server default 20).
     */
    GetChannels limit(@Nullable Integer limit);

    /**
     * Optional opaque cursor for pagination (from a previous result's {@code next.getCursor()}).
     */
    GetChannels cursor(@Nullable String cursor);
}