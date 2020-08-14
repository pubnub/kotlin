package com.pubnub.api.models.consumer.files;

import com.pubnub.api.models.consumer.PNPage;
import lombok.Data;
import lombok.NonNull;

import java.util.Collection;

@Data
public class PNListFilesResult {
    @NonNull
    private final int count;
    private final PNPage.Next next;
    @NonNull
    private final int status;
    @NonNull
    private final Collection<PNUploadedFile> data;
}