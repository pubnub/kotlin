package com.pubnub.api.endpoints.files;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.files.requiredparambuilder.ChannelFileNameFileIdBuilder;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.files.PNFileUrlResult;
import com.pubnub.internal.CorePubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;

public class GetFileUrl extends DelegatingEndpoint<PNFileUrlResult> {

    private final String channel;
    private final String fileId;
    private final String fileName;

    public GetFileUrl(String channel, String fileId, String fileName, CorePubNubClient pubnub) {
        super(pubnub);
        this.channel = channel;
        this.fileId = fileId;
        this.fileName = fileName;
    }

    @Override
    protected ExtendedRemoteAction<PNFileUrlResult> createAction() {
        return pubnub.getFileUrl(
                channel,
                fileName,
                fileId
        );
    }

    public static class Builder extends ChannelFileNameFileIdBuilder<GetFileUrl> {
        private Builder(BuilderSteps.ChannelStep<FilesBuilderSteps.FileNameStep<FilesBuilderSteps.FileIdStep<GetFileUrl>>> builder) {
            super(builder);
        }
    }

    public static Builder builder(CorePubNubClient pubNub) {
        return new Builder(ChannelFileNameFileIdBuilder.create((channel, fileName, fileId) ->
                new GetFileUrl(channel, fileName, fileId, pubNub)));
    }
}