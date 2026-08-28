package com.pubnub.api.java.models.consumer.datasync.user;

import com.pubnub.api.java.models.consumer.datasync.PNDataSyncPage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Result of {@code pubnub.dataSync().getUsers()}.
 * DataSync list endpoints are cursor-based (unlike App Context's page-based paging).
 */
@Getter
@NoArgsConstructor
@ToString
public class DataSyncGetUsersResult {
    private int status;
    private List<DataSyncUser> data;

    /**
     * Cursor-based pagination metadata. Always present (an empty page when the server sends no
     * {@code meta}); read {@link PNDataSyncPage#isHasNext()} to detect the last page.
     */
    @NotNull
    private PNDataSyncPage next;

    public DataSyncGetUsersResult(int status, List<DataSyncUser> data, @NotNull PNDataSyncPage next) {
        this.status = status;
        this.data = data;
        this.next = next;
    }
}
