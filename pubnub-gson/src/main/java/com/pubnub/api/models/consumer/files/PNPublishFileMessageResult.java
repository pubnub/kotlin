package com.pubnub.api.models.consumer.files;

import lombok.Data;
import lombok.NonNull;

@Data
public class PNPublishFileMessageResult {
    @NonNull
    private final long timetoken;
}
