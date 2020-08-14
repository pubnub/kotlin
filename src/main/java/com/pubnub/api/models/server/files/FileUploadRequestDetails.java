package com.pubnub.api.models.server.files;

import com.pubnub.api.models.consumer.files.PNFile;
import lombok.Data;

import java.util.List;

@Data
public class FileUploadRequestDetails {
    private final Integer status;
    private final PNFile data;
    private final String url;
    private final String method;
    private final String expirationDate;
    private final FormField keyFormField;
    private final List<FormField> formFields;
}
