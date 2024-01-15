package com.pubnub.api.models.server.files;

import com.pubnub.api.models.consumer.files.PNBaseFile;
import lombok.Data;

@Data
public class FileUploadNotification {
    private final Object message;
    private final PNBaseFile file;
}
