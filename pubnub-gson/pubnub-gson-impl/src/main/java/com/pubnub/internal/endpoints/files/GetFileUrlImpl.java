package com.pubnub.internal.endpoints.files;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.files.GetFileUrl;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.models.consumer.files.PNFileUrlResult;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.IdentityMappingEndpoint;
import com.pubnub.internal.endpoints.files.requiredparambuilder.ChannelFileNameFileIdBuilder;
import org.jetbrains.annotations.NotNull;

public class GetFileUrlImpl extends IdentityMappingEndpoint<PNFileUrlResult> implements GetFileUrl {

    private final String channel;
    private final String fileId;
    private final String fileName;

    public GetFileUrlImpl(String channel, String fileId, String fileName, PubNubCore pubnub) {
        super(pubnub);
        this.channel = channel;
        this.fileId = fileId;
        this.fileName = fileName;
    }

    @Override
    @NotNull
    protected EndpointInterface<PNFileUrlResult> createAction() {
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

    public static GetFileUrlImpl.Builder builder(PubNubCore pubNub) {
        return new GetFileUrlImpl.Builder(ChannelFileNameFileIdBuilder.create((channel, fileName, fileId) ->
                new GetFileUrlImpl(channel, fileName, fileId, pubNub)));
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channel == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING);
        }
    }
}