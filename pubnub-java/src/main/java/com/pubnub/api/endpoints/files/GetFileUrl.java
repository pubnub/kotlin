package com.pubnub.api.endpoints.files;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.files.requiredparambuilder.ChannelFileNameFileIdBuilder;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.files.PNFileUrlResult;
import com.pubnub.internal.PubNubImpl;

public class GetFileUrl extends Endpoint<PNFileUrlResult> {

    private final String channel;
    private final  String fileId;
    private final  String fileName;

    public GetFileUrl(String channel, String fileId, String fileName, PubNubImpl pubnub) {
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

    public static Builder builder(PubNubImpl pubNub) {
        return new Builder(ChannelFileNameFileIdBuilder.create((channel, fileName, fileId) ->
                new GetFileUrl(channel, fileName, fileId, pubNub)));
    }
}