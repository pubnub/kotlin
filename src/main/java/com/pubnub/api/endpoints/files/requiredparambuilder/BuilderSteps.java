package com.pubnub.api.endpoints.files.requiredparambuilder;

import java.io.InputStream;

public interface BuilderSteps {
    interface ChannelStep<T> {
        T channel(String channel);
    }

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
