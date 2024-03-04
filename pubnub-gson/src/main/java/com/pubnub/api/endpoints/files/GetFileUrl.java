package com.pubnub.api.endpoints.files;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.models.consumer.files.PNFileUrlResult;

public interface GetFileUrl extends Endpoint<PNFileUrlResult> {
    interface Builder extends BuilderSteps.ChannelStep<FilesBuilderSteps.FileNameStep<FilesBuilderSteps.FileIdStep<GetFileUrl>>> {

    }
}
