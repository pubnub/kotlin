package com.pubnub.api.java.endpoints.files;

import com.pubnub.api.java.endpoints.BuilderSteps;
import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult;

public interface PublishFileMessage extends Endpoint<PNPublishFileMessageResult> {

    PublishFileMessage message(Object message);

    PublishFileMessage meta(Object meta);

    PublishFileMessage ttl(Integer ttl);

    PublishFileMessage shouldStore(Boolean shouldStore);

    PublishFileMessage customMessageType(String customMessageType);

    interface Builder extends BuilderSteps.ChannelStep<FilesBuilderSteps.FileNameStep<FilesBuilderSteps.FileIdStep<PublishFileMessage>>> {

    }
}
