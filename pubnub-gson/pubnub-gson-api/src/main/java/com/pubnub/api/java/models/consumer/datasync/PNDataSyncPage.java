package com.pubnub.api.java.models.consumer.datasync;

import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

/**
 * Cursor-based pagination metadata returned by DataSync list endpoints (e.g. {@code getChannels}).
 */
@Getter
@ToString
public class PNDataSyncPage {
    /**
     * Opaque cursor for the next page, or {@code null} when there are no more results.
     */
    @Nullable
    private final String cursor;

    /**
     * Whether there are more results after this page. This — not a {@code null} cursor — is the
     * authoritative "no more pages" signal.
     */
    private final boolean hasNext;

    /**
     * The limit applied to this page (may differ from the requested limit).
     */
    @Nullable
    private final Integer limit;

    public PNDataSyncPage(@Nullable String cursor, boolean hasNext, @Nullable Integer limit) {
        this.cursor = cursor;
        this.hasNext = hasNext;
        this.limit = limit;
    }
}