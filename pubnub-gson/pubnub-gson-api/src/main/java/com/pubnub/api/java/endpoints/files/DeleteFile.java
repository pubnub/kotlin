package com.pubnub.api.java.endpoints.files;

import com.pubnub.api.java.endpoints.BuilderSteps;
import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.models.consumer.files.PNDeleteFileResult;

public interface DeleteFile extends Endpoint<PNDeleteFileResult> {
    interface Builder extends BuilderSteps.ChannelStep<FilesBuilderSteps.FileNameStep<FilesBuilderSteps.FileIdStep<DeleteFile>>> {

    }
}
