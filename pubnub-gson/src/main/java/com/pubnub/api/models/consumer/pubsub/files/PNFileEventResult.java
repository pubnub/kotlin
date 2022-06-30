package com.pubnub.api.models.consumer.pubsub.files;

import com.pubnub.api.models.consumer.files.PNDownloadableFile;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class PNFileEventResult {
    @NonNull
    private final String channel;
    @NonNull
    private final Long timetoken;
    private final String publisher;
    private final Object message;
    @NonNull
    private final PNDownloadableFile file;
}
