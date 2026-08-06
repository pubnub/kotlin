package com.pubnub.api.java.models.consumer.datasync.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Result of {@code pubnub.dataSync().getUsers()}.
 * DataSync list endpoints are cursor-based (unlike App Context's page-based paging).
 */
@Getter
@NoArgsConstructor
@ToString
public class PNGetUsersResult {
    private int status;
    private List<PNUser> data;

    /**
     * Opaque cursor for the next page, or {@code null} when there are no more results.
     */
    @Nullable
    private String next;

    private boolean hasNext;

    /**
     * The limit applied to this page (may differ from the requested limit).
     */
    @Nullable
    private Integer limit;

    public PNGetUsersResult(int status, List<PNUser> data, @Nullable String next, boolean hasNext, @Nullable Integer limit) {
        this.status = status;
        this.data = data;
        this.next = next;
        this.hasNext = hasNext;
        this.limit = limit;
    }
}
