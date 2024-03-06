package com.pubnub.internal.endpoints.files;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.BuilderSteps.ChannelStep;
import com.pubnub.api.endpoints.files.DeleteFile;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps.FileIdStep;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps.FileNameStep;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.files.PNDeleteFileResult;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import com.pubnub.internal.endpoints.files.requiredparambuilder.ChannelFileNameFileIdBuilder;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class DeleteFileImpl extends DelegatingEndpoint<PNDeleteFileResult> implements DeleteFile {

    private final String channel;
    private final String fileId;
    private final String fileName;

    public DeleteFileImpl(String channel, String fileName, String fileId, PubNubCore pubnub) {
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

    public static DeleteFileImpl.Builder builder(PubNubCore pubnub) {
        return new DeleteFileImpl.Builder(ChannelFileNameFileIdBuilder.create((channel, fileName, fileId) ->
                new DeleteFileImpl(channel, fileName, fileId, pubnub)));
    }

    public static class Builder extends ChannelFileNameFileIdBuilder<DeleteFile> implements DeleteFile.Builder {
        public Builder(ChannelStep<FileNameStep<FileIdStep<DeleteFile>>> builder) {
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