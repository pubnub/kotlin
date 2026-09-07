package com.pubnub.api.java.endpoints.datasync.membership;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncSortField;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncGetMembershipsResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @see com.pubnub.api.java.datasync.DataSync#getMemberships()
 */
public interface GetMemberships extends Endpoint<PNDataSyncGetMembershipsResult> {
    /**
     * Optional Channel identifier to filter by. {@code channelId} and {@link #userId(String)} are independent,
     * AND-ed equality filters on fixed sides of the membership: {@code channelId} matches the Channel (entity A)
     * side and {@code userId} matches the User (entity B) side. Supplying only one narrows to memberships on
     * that side; supplying both returns the membership(s) matching both (i.e. that specific channel-user pair);
     * supplying neither lists all readable memberships.
     */
    GetMemberships channelId(@Nullable String channelId);

    /**
     * Optional User identifier to filter by. See {@link #channelId(String)} for how the two combine.
     */
    GetMemberships userId(@Nullable String userId);

    /**
     * Restricts results to a single version of the {@code Membership} class. If not set, every version is
     * returned.
     */
    GetMemberships classVersion(@Nullable Integer classVersion);

    /**
     * Optional filter expression. For the built-in {@code Membership} class, filtering is allowed only on the
     * built-in fields {@code id}, {@code createdAt}, {@code updatedAt}, and {@code status} (case-sensitive,
     * exactly as spelled), which are filterable and sortable on both the {@code filterFast} and
     * {@link #filter(String)} paths — except that {@code status} may be excluded when the class declares it as a
     * projected field and the token cannot fully reach it.
     *
     * <p>{@code filterFast} is strongly consistent — it always reflects the latest writes — but accepts fewer
     * conditions than {@link #filter(String)}; a limit on the number of conditions applies and can be adjusted
     * by PubNub support (see the PubNub DataSync documentation for the current limit). For larger or more
     * complex queries use {@link #filter(String)}. At most one of {@code filterFast} and {@code filter} may be
     * supplied; sending both is rejected with an error.
     */
    GetMemberships filterFast(@Nullable String filterFast);

    /**
     * Optional advanced filter expression. Uses the same syntax as {@link #filterFast(String)} and, for the
     * built-in {@code Membership} class, targets the same built-in fields. It is not subject to the same
     * condition limit, so use it for larger or more complex queries. The trade-off is consistency:
     * {@code filter} is eventually consistent (recent writes may not yet be reflected), whereas
     * {@link #filterFast(String)} is strongly consistent. At most one of {@code filterFast} and {@code filter}
     * may be supplied; sending both is rejected with an error.
     */
    GetMemberships filter(@Nullable String filter);

    /**
     * Optional sort criteria applied in order; each {@link PNDataSyncSortField} sorts on a property either
     * ascending (default) or descending. For the built-in {@code Membership} class, the sortable set is the
     * built-in fields {@code id}, {@code createdAt}, {@code updatedAt}, and {@code status} (with the same
     * {@code status} caveat as {@link #filterFast(String)}).
     */
    GetMemberships sort(@Nullable List<PNDataSyncSortField> sort);

    /**
     * Optional page size (1–100, server default 20).
     */
    GetMemberships limit(@Nullable Integer limit);

    /**
     * Optional opaque cursor for pagination (from a previous result's {@code next.getCursor()}).
     */
    GetMemberships cursor(@Nullable String cursor);
}