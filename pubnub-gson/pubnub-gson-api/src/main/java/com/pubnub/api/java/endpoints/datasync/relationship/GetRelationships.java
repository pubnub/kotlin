package com.pubnub.api.java.endpoints.datasync.relationship;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncSortField;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncGetRelationshipsResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @see com.pubnub.api.java.datasync.DataSync#getRelationships(String)
 */
public interface GetRelationships extends Endpoint<PNDataSyncGetRelationshipsResult> {
    /**
     * Optional entity A identifier to filter by. {@code entityAId} and {@link #entityBId(String)} are
     * independent, AND-ed equality filters on the two sides of the relationship. Supplying only one narrows to
     * relationships on that side; supplying both returns the relationships matching both; supplying neither
     * lists all readable relationships in the class. This is not a pair-only lookup API.
     */
    GetRelationships entityAId(@Nullable String entityAId);

    /**
     * Optional entity B identifier to filter by. See {@link #entityAId(String)} for how the two combine.
     */
    GetRelationships entityBId(@Nullable String entityBId);

    /**
     * Restricts results to a single version of the relationship class. If not set, every version is returned.
     */
    GetRelationships classVersion(@Nullable Integer classVersion);

    /**
     * Optional filter expression. {@code filterFast} is strongly consistent — it always reflects the latest
     * writes — but accepts fewer conditions than {@link #filter(String)}; a limit on the number of conditions
     * applies and can be adjusted by PubNub support (see the PubNub DataSync documentation for the current
     * limit). For larger or more complex queries use {@link #filter(String)}. At most one of {@code filterFast}
     * and {@code filter} may be supplied; sending both is rejected with an error.
     */
    GetRelationships filterFast(@Nullable String filterFast);

    /**
     * Optional advanced filter expression. Uses the same syntax as {@link #filterFast(String)}. It is not
     * subject to the same condition limit, so use it for larger or more complex queries. The trade-off is
     * consistency: {@code filter} is eventually consistent (recent writes may not yet be reflected), whereas
     * {@link #filterFast(String)} is strongly consistent. At most one of {@code filterFast} and {@code filter}
     * may be supplied; sending both is rejected with an error.
     */
    GetRelationships filter(@Nullable String filter);

    /**
     * Optional sort criteria applied in order; each {@link PNDataSyncSortField} sorts on a property either
     * ascending (default) or descending.
     */
    GetRelationships sort(@Nullable List<PNDataSyncSortField> sort);

    /**
     * Optional page size (1–100, server default 20).
     */
    GetRelationships limit(@Nullable Integer limit);

    /**
     * Optional opaque cursor for pagination (from a previous result's {@code next.getCursor()}).
     */
    GetRelationships cursor(@Nullable String cursor);
}