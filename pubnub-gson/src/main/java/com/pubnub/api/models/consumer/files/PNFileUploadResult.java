package com.pubnub.api.models.consumer.files;

import lombok.Data;
import lombok.NonNull;

@Data
public class PNFileUploadResult {
    @NonNull
    private final long timetoken;
    @NonNull
    private final int status;
    @NonNull
    private final PNBaseFile file;
}
