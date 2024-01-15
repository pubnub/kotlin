package com.pubnub.api.models.server.files;

import com.google.gson.annotations.SerializedName;
import com.pubnub.api.models.consumer.files.PNUploadedFile;
import lombok.Data;

import java.util.List;

@Data
public class GeneratedUploadUrlResponse {
    private final Integer status;
    private final PNUploadedFile data;
    @SerializedName("file_upload_request")
    private final FileUploadRequest fileUploadRequest;

    @Data
    public static class FileUploadRequest {
        private final String url;
        private final String method;
        @SerializedName("expiration_date")
        private final String expirationDate;
        @SerializedName("form_fields")
        private final List<FormField> formFields;
    }
}
