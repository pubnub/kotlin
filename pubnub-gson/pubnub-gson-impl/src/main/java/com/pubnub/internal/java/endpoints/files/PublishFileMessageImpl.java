package com.pubnub.internal.java.endpoints.files;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.endpoints.BuilderSteps;
import com.pubnub.api.java.endpoints.files.PublishFileMessage;
import com.pubnub.api.java.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult;
import com.pubnub.internal.java.endpoints.PassthroughEndpoint;
import com.pubnub.internal.java.endpoints.files.requiredparambuilder.ChannelFileNameFileIdBuilder;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Setter
@Accessors(chain = true, fluent = true)
public class PublishFileMessageImpl extends PassthroughEndpoint<PNPublishFileMessageResult> implements PublishFileMessage {

    @Setter
    private Object message;
    @Setter
    private Object meta;
    @Setter
    private Integer ttl;
    @Setter
    private Boolean shouldStore;
    @Setter
    private String customMessageType;

    private final String channel;
    private final String fileName;
    private final String fileId;

    public PublishFileMessageImpl(String channel, String fileName, String fileId, PubNub pubnub) {
        super(pubnub);
        this.channel = channel;
        this.fileName = fileName;
        this.fileId = fileId;
    }

    @Override
    @NotNull
    protected Endpoint<PNPublishFileMessageResult> createRemoteAction() {
        return pubnub.publishFileMessage(
                channel,
                fileName,
                fileId,
                message,
                meta,
                ttl,
                shouldStore,
                customMessageType
        );
    }

    public static PublishFileMessage.Builder builder(PubNub pubNub) {
        return new Builder(ChannelFileNameFileIdBuilder.create((channel, fileName, fileId) ->
                new PublishFileMessageImpl(channel, fileName, fileId, pubNub)));
    }

    public static class Builder extends ChannelFileNameFileIdBuilder<PublishFileMessage>
            implements PublishFileMessage.Builder {
        private Builder(BuilderSteps.ChannelStep<FilesBuilderSteps.FileNameStep<FilesBuilderSteps.FileIdStep<PublishFileMessage>>> builder) {
            super(builder);
        }
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channel == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING);
        }
    }
}