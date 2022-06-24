package com.pubnub.api.models.consumer.files;

import lombok.Data;
import lombok.NonNull;

import java.io.InputStream;

@Data
public class PNDownloadFileResult {
    @NonNull
    private final String fileName;
    private final InputStream byteStream;
}
