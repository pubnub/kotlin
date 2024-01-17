package com.pubnub.api.endpoints.files;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.BuilderSteps.ChannelStep;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.files.requiredparambuilder.ChannelFileNameFileIdBuilder;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps.FileIdStep;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps.FileNameStep;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.files.PNDeleteFileResult;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class DeleteFile extends ValidatingEndpoint<PNDeleteFileResult> {

    private final String channel;
    private final String fileId;
    private final String fileName;

    public DeleteFile(String channel, String fileName, String fileId, com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
        this.channel = channel;
        this.fileName = fileName;
        this.fileId = fileId;
    }

    @Override
    protected Endpoint<PNDeleteFileResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.deleteFile(
                channel,
                fileName,
                fileId
        ));
    }

    public static class Builder extends ChannelFileNameFileIdBuilder<DeleteFile> {
        public Builder(ChannelStep<FileNameStep<FileIdStep<DeleteFile>>> builder) {
            super(builder);
        }
    }

    public static Builder builder(com.pubnub.internal.PubNub pubnub) {
        return new Builder(ChannelFileNameFileIdBuilder.create((channel, fileName, fileId) ->
                new DeleteFile(channel, fileName, fileId, pubnub)));
    }
}