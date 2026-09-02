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
     * Optional filter expression. Filtering is only allowed on properties the entity class marks as filterable;
     * filtering on any other property returns a server error. Each property in the class declares a
     * {@code filtering} mode — {@code none}, {@code simple}, or {@code full} — and the mode is cumulative: a
     * {@code simple} property is usable with {@code filterFast} (and {@link #sort(List)} on the
     * {@code filterFast} path); a {@code full} property is usable with both {@code filterFast} and
     * {@link #filter(String)}; a {@code none} property is not filterable at all. So a {@code simple} property
     * works here but is rejected by {@link #filter(String)}. The filterable set depends on the entity class
     * supplied to {@code getEntities(className)} — an Entity has no built-in default set. In addition, the
     * built-in fields {@code id}, {@code createdAt}, {@code updatedAt}, and {@code status} (case-sensitive,
     * exactly as spelled) behave as {@code full} and are always filterable and sortable on any class, on both
     * the {@code filterFast} and {@link #filter(String)} paths, regardless of its declared properties.
     *
     * <p>{@code filterFast} is strongly consistent — it always reflects the latest writes — but accepts fewer
     * conditions than {@link #filter(String)}; a limit on the number of conditions applies and can be
     * adjusted by PubNub support (see the PubNub DataSync documentation for the current limit). For larger or
     * more complex queries use {@link #filter(String)}. At most one of {@code filterFast} and
     * {@code filter} may be supplied; sending both is rejected with an error.
     */
    GetEntities filterFast(@Nullable String filterFast);

    /**
     * Optional advanced filter expression. Uses the same syntax as {@link #filterFast(String)}, but only
     * properties whose {@code filtering} mode is {@code full} (plus the built-in fields) are usable here — a
     * {@code simple} property that works with {@link #filterFast(String)} is rejected by {@code filter}. It is
     * not subject to the same condition limit, so use it for larger or more complex queries. The trade-off is
     * consistency: {@code filter} is eventually consistent (recent writes may not yet be reflected), whereas
     * {@link #filterFast(String)} is strongly consistent. At most one of {@code filterFast} and {@code filter}
     * may be supplied; sending both is rejected with an error.
     */
    GetEntities filter(@Nullable String filter);

    /**
     * Optional sort criteria applied in order; each {@link PNDataSyncSortField} sorts on a payload
     * property either ascending (default) or descending. Sorting is governed by the same {@code filtering}-mode
     * gate as filtering: a {@code simple} property is sortable on the strongly-consistent path
     * ({@link #filterFast(String)}, or a sort-only / {@link #cursor(String)} request with neither filter); a
     * {@code full} property is sortable on that path and on the {@link #filter(String)} path. The built-in
     * fields {@code id}, {@code createdAt}, {@code updatedAt}, and {@code status} are always sortable.
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
