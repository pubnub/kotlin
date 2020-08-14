package com.pubnub.api.models.consumer.files;

import lombok.Data;
import lombok.NonNull;

@Data
public class PNDeleteFileResult {
    @NonNull
    private final int status;
}
