package com.pubnub.api.java.models.consumer.datasync.channel;

import com.pubnub.api.java.models.consumer.datasync.PNDataSyncPage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Result of {@code pubnub.dataSync().getChannels()}.
 * DataSync list endpoints are cursor-based.
 */
@Getter
@NoArgsConstructor
@ToString
public class PNDataSyncGetChannelsResult {
    private int status;
    private List<PNDataSyncChannel> data;

    /**
     * Cursor-based pagination metadata. Always present (an empty page when the server sends no
     * {@code meta}); read {@link PNDataSyncPage#isHasNext()} to detect the last page.
     */
    @NotNull
    private PNDataSyncPage next;

    public PNDataSyncGetChannelsResult(int status, List<PNDataSyncChannel> data, @NotNull PNDataSyncPage next) {
        this.status = status;
        this.data = data;
        this.next = next;
    }
}