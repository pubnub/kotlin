package com.pubnub.api.endpoints.files;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.files.requiredparambuilder.ChannelFileNameFileIdBuilder;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class PublishFileMessage extends DelegatingEndpoint<PNPublishFileMessageResult> {

    @Setter
    private Object message;
    @Setter
    private Object meta;
    @Setter
    private Integer ttl;
    @Setter
    private Boolean shouldStore;

    private final String channel;
    private final String fileName;
    private final String fileId;

    public PublishFileMessage(String channel, String fileName, String fileId, PubNubCore pubnub) {
        super(pubnub);
        this.channel = channel;
        this.fileName = fileName;
        this.fileId = fileId;
    }

    @Override
    protected ExtendedRemoteAction<PNPublishFileMessageResult> createAction() {
        return pubnub.publishFileMessage(
                channel,
                fileName,
                fileId,
                message,
                meta,
                ttl,
                shouldStore
        );
    }

    public static class Builder
            extends ChannelFileNameFileIdBuilder<PublishFileMessage> {
        private Builder(BuilderSteps.ChannelStep<FilesBuilderSteps.FileNameStep<FilesBuilderSteps.FileIdStep<PublishFileMessage>>> builder) {
            super(builder);
        }
    }

    public static Builder builder(PubNubCore pubNub) {
        return new Builder(ChannelFileNameFileIdBuilder.create((channel, fileName, fileId) ->
                new PublishFileMessage(channel, fileName, fileId, pubNub)));
    }
}