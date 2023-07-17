package com.pubnub.api.models.consumer.files;

import lombok.Data;
import lombok.NonNull;

@Data
public class PNFileUrlResult {
    @NonNull
    private final String url;
}
