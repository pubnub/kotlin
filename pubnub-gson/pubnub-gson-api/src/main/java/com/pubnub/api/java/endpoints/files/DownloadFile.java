package com.pubnub.api.java.endpoints.files;

import com.pubnub.api.java.endpoints.BuilderSteps;
import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.models.consumer.files.PNDownloadFileResult;

public interface DownloadFile extends Endpoint<PNDownloadFileResult> {

    DownloadFile cipherKey(String cipherKey);

    interface Builder extends BuilderSteps.ChannelStep<FilesBuilderSteps.FileNameStep<FilesBuilderSteps.FileIdStep<DownloadFile>>> {
    }
}
