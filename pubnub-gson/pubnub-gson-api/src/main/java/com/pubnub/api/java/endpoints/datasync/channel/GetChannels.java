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
     * but a custom class or subclass may declare additional filterable properties. In addition, the built-in
     * fields {@code id}, {@code createdAt}, {@code updatedAt}, and {@code status} (case-sensitive, exactly as
     * spelled) are always filterable and sortable on any class, regardless of its declared properties.
     *
     * <p>{@code filterFast} is strongly consistent — it always reflects the latest writes — but accepts fewer
     * conditions than {@link #filter(String)}; a limit on the number of conditions applies and can be
     * adjusted by PubNub support (see the PubNub DataSync documentation for the current limit). For larger or
     * more complex queries use {@link #filter(String)}. At most one of {@code filterFast} and
     * {@code filter} may be supplied; sending both is rejected with an error.
     */
    GetChannels filterFast(@Nullable String filterFast);

    /**
     * Optional advanced filter expression. Uses the same syntax and filterable-property rules as
     * {@link #filterFast(String)} and is not subject to the same condition limit, so use it for larger or more
     * complex queries. The trade-off is consistency: {@code filter} is eventually consistent (recent
     * writes may not yet be reflected), whereas {@link #filterFast(String)} is strongly consistent. At most one of
     * {@code filterFast} and {@code filter} may be supplied; sending both is rejected with an error.
     */
    GetChannels filter(@Nullable String filter);

    /**
     * Optional sort criteria applied in order; each {@link PNDataSyncSortField} sorts on a payload
     * property either ascending (default) or descending. Sorting is governed by the same filterable-property
     * rule as {@link #filterFast(String)} (default {@code Channel} class: {@code name} and {@code type}, plus
     * the built-in fields {@code id}, {@code createdAt}, {@code updatedAt}, and {@code status}).
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