package com.pubnub.api.java.models.consumer.datasync;

import lombok.Getter;
import lombok.ToString;

/**
 * A single DataSync sort criterion applied by the list endpoints (e.g. {@code getChannels}).
 *
 * <p>Unlike App Context v2's {@code PNSortKey} (which fixes the sortable fields to an enum), DataSync sorts
 * on arbitrary payload properties, so {@code property} is free-form.
 */
@Getter
@ToString
public class PNDataSyncSortField {
    private final String property;
    private final boolean ascending;

    /**
     * @param property  The payload property name to sort by.
     * @param ascending Sort direction; {@code true} is ascending, {@code false} is descending.
     */
    public PNDataSyncSortField(String property, boolean ascending) {
        this.property = property;
        this.ascending = ascending;
    }

    /**
     * Ascending sort on {@code property}.
     */
    public PNDataSyncSortField(String property) {
        this(property, true);
    }
}