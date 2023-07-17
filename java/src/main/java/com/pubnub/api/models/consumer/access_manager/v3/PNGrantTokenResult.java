package com.pubnub.api.models.consumer.access_manager.v3;

import lombok.Data;
import lombok.NonNull;

@Data
public class PNGrantTokenResult {
    @NonNull
    private final String token;
}
