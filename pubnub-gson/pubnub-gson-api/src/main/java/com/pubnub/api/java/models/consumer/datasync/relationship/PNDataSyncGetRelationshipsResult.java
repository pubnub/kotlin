package com.pubnub.api.java.models.consumer.datasync.relationship;

import com.pubnub.api.java.models.consumer.datasync.PNDataSyncPage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Result of {@code pubnub.dataSync().getRelationships()}.
 * DataSync list endpoints are cursor-based.
 */
@Getter
@NoArgsConstructor
@ToString
public class PNDataSyncGetRelationshipsResult {
    private int status;
    private List<PNDataSyncRelationship> data;

    /**
     * Cursor-based pagination metadata. Always present (an empty page when the server sends no
     * {@code meta}); read {@link PNDataSyncPage#isHasNext()} to detect the last page.
     */
    @NotNull
    private PNDataSyncPage next;

    public PNDataSyncGetRelationshipsResult(int status, List<PNDataSyncRelationship> data, @NotNull PNDataSyncPage next) {
        this.status = status;
        this.data = data;
        this.next = next;
    }
}
