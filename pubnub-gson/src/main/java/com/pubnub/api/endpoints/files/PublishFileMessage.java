package com.pubnub.api.endpoints.files;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult;

public interface PublishFileMessage extends Endpoint<PNPublishFileMessageResult> {

    PublishFileMessage message(Object message);

    PublishFileMessage meta(Object meta);

    PublishFileMessage ttl(Integer ttl);

    PublishFileMessage shouldStore(Boolean shouldStore);

    interface Builder extends BuilderSteps.ChannelStep<FilesBuilderSteps.FileNameStep<FilesBuilderSteps.FileIdStep<PublishFileMessage>>> {

    }
}
