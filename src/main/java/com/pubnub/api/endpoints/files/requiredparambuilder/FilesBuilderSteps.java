package com.pubnub.api.endpoints.files.requiredparambuilder;

import com.pubnub.api.endpoints.BuilderSteps;

import java.io.InputStream;

public interface FilesBuilderSteps extends BuilderSteps {

    interface FileNameStep<T> {
        T fileName(String fileName);
    }

    interface InputStreamStep<T> {
        T inputStream(InputStream inputStream);
    }

    interface FileIdStep<T> {
        T fileId(String fileId);
    }

}
