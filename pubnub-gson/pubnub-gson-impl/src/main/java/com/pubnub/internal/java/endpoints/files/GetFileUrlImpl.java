package com.pubnub.internal.java.endpoints.files;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.endpoints.BuilderSteps;
import com.pubnub.api.java.endpoints.files.GetFileUrl;
import com.pubnub.api.java.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.models.consumer.files.PNFileUrlResult;
import com.pubnub.internal.java.endpoints.PassthroughEndpoint;
import com.pubnub.internal.java.endpoints.files.requiredparambuilder.ChannelFileNameFileIdBuilder;
import org.jetbrains.annotations.NotNull;

public class GetFileUrlImpl extends PassthroughEndpoint<PNFileUrlResult> implements GetFileUrl {

    private final String channel;
    private final String fileId;
    private final String fileName;

    public GetFileUrlImpl(String channel, String fileId, String fileName, PubNub pubnub) {
        super(pubnub);
        this.channel = channel;
        this.fileId = fileId;
        this.fileName = fileName;
    }

    @Override
    @NotNull
    protected Endpoint<PNFileUrlResult> createRemoteAction() {
        return pubnub.getFileUrl(
                channel,
                fileName,
                fileId
        );
    }

    public static class Builder extends ChannelFileNameFileIdBuilder<GetFileUrl> implements GetFileUrl.Builder {
        private Builder(BuilderSteps.ChannelStep<FilesBuilderSteps.FileNameStep<FilesBuilderSteps.FileIdStep<GetFileUrl>>> builder) {
            super(builder);
        }
    }

    public static Builder builder(PubNub pubNub) {
        return new Builder(ChannelFileNameFileIdBuilder.create((channel, fileName, fileId) ->
                new GetFileUrlImpl(channel, fileName, fileId, pubNub)));
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channel == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING);
        }
    }
}