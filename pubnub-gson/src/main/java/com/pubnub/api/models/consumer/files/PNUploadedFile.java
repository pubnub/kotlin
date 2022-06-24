package com.pubnub.api.models.consumer.files;

import lombok.Data;
import lombok.NonNull;

@Data
public class PNUploadedFile implements PNFile {
    @NonNull
    private final String id;
    @NonNull
    private final String name;
    @NonNull
    private final Integer size;
    @NonNull
    private final String created;
}
