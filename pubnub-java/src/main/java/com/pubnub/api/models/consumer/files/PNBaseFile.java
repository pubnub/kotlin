package com.pubnub.api.models.consumer.files;

import lombok.Data;
import lombok.NonNull;

@Data
public class PNBaseFile implements PNFile {
    @NonNull
    private final String id;
    @NonNull
    private final String name;
}
