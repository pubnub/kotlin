package com.pubnub.internal.java.endpoints.files;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.endpoints.BuilderSteps.ChannelStep;
import com.pubnub.api.java.endpoints.files.DeleteFile;
import com.pubnub.api.java.endpoints.files.requiredparambuilder.FilesBuilderSteps.FileIdStep;
import com.pubnub.api.java.endpoints.files.requiredparambuilder.FilesBuilderSteps.FileNameStep;
import com.pubnub.api.models.consumer.files.PNDeleteFileResult;
import com.pubnub.internal.java.endpoints.IdentityMappingEndpoint;
import com.pubnub.internal.java.endpoints.files.requiredparambuilder.ChannelFileNameFileIdBuilder;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Accessors(chain = true, fluent = true)
public class DeleteFileImpl extends IdentityMappingEndpoint<PNDeleteFileResult> implements DeleteFile {

    private final String channel;
    private final String fileId;
    private final String fileName;

    public DeleteFileImpl(String channel, String fileName, String fileId, PubNub pubnub) {
        super(pubnub);
        this.channel = channel;
        this.fileName = fileName;
        this.fileId = fileId;
    }

    @Override
    @NotNull
    protected Endpoint<PNDeleteFileResult> createAction() {
        return pubnub.deleteFile(
                channel,
                fileName,
                fileId
        );
    }

    public static Builder builder(PubNub pubnub) {
        return new Builder(ChannelFileNameFileIdBuilder.create((channel, fileName, fileId) ->
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