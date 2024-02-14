package com.pubnub.api.endpoints.files;

import com.pubnub.api.endpoints.BuilderSteps.ChannelStep;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.files.requiredparambuilder.ChannelFileNameFileIdBuilder;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps.FileIdStep;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps.FileNameStep;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.files.PNDeleteFileResult;
import com.pubnub.internal.PubNubImpl;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class DeleteFile extends Endpoint<PNDeleteFileResult> {

    private final String channel;
    private final String fileId;
    private final String fileName;

    public DeleteFile(String channel, String fileName, String fileId, PubNubImpl pubnub) {
        super(pubnub);
        this.channel = channel;
        this.fileName = fileName;
        this.fileId = fileId;
    }

    @Override
    protected ExtendedRemoteAction<PNDeleteFileResult> createAction() {
        return pubnub.deleteFile(
                channel,
                fileName,
                fileId
        );
    }

    public static class Builder extends ChannelFileNameFileIdBuilder<DeleteFile> {
        public Builder(ChannelStep<FileNameStep<FileIdStep<DeleteFile>>> builder) {
            super(builder);
        }
    }

    public static Builder builder(PubNubImpl pubnub) {
        return new Builder(ChannelFileNameFileIdBuilder.create((channel, fileName, fileId) ->
                new DeleteFile(channel, fileName, fileId, pubnub)));
    }
}