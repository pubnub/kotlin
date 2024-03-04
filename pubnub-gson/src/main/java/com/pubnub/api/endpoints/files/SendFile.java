package com.pubnub.api.endpoints.files;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.models.consumer.files.PNFileUploadResult;

public interface SendFile extends Endpoint<PNFileUploadResult> {

    SendFile message(Object message);

    SendFile meta(Object meta);

    SendFile ttl(Integer ttl);

    SendFile shouldStore(Boolean shouldStore);

    SendFile cipherKey(String cipherKey);

    interface Builder extends BuilderSteps.ChannelStep<FilesBuilderSteps.FileNameStep<FilesBuilderSteps.InputStreamStep<SendFile>>> {

    }
}
