package com.pubnub.api.java.models.consumer.datasync.membership;

import com.pubnub.api.java.models.consumer.datasync.PNDataSyncPage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Result of {@code pubnub.dataSync().getMemberships()}.
 * DataSync list endpoints are cursor-based.
 */
@Getter
@NoArgsConstructor
@ToString
public class PNDataSyncGetMembershipsResult {
    private int status;
    private List<PNDataSyncMembership> data;

    /**
     * Cursor-based pagination metadata. Always present (an empty page when the server sends no
     * {@code meta}); read {@link PNDataSyncPage#isHasNext()} to detect the last page.
     */
    @NotNull
    private PNDataSyncPage next;

    public PNDataSyncGetMembershipsResult(int status, List<PNDataSyncMembership> data, @NotNull PNDataSyncPage next) {
        this.status = status;
        this.data = data;
        this.next = next;
    }
}