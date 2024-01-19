package com.pubnub.api.endpoints.files;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.files.requiredparambuilder.ChannelFileNameFileIdBuilder;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.files.PNFileUrlResult;

public class GetFileUrl extends ValidatingEndpoint<PNFileUrlResult> {

    private final String channel;
    private final  String fileId;
    private final  String fileName;

    public GetFileUrl(String channel, String fileId, String fileName, com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
        this.channel = channel;
        this.fileId = fileId;
        this.fileName = fileName;
    }

    @Override
    protected Endpoint<PNFileUrlResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.getFileUrl(
                channel,
                fileName,
                fileId
        ));
    }

    public static class Builder extends ChannelFileNameFileIdBuilder<GetFileUrl> {
        private Builder(BuilderSteps.ChannelStep<FilesBuilderSteps.FileNameStep<FilesBuilderSteps.FileIdStep<GetFileUrl>>> builder) {
            super(builder);
        }
    }

    public static Builder builder(com.pubnub.internal.PubNub pubNub) {
        return new Builder(ChannelFileNameFileIdBuilder.create((channel, fileName, fileId) ->
                new GetFileUrl(channel, fileName, fileId, pubNub)));
    }
}