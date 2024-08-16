package com.pubnub.internal.java.endpoints.files;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.endpoints.BuilderSteps;
import com.pubnub.api.java.endpoints.files.DownloadFile;
import com.pubnub.api.java.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.models.consumer.files.PNDownloadFileResult;
import com.pubnub.internal.java.endpoints.IdentityMappingEndpoint;
import com.pubnub.internal.java.endpoints.files.requiredparambuilder.ChannelFileNameFileIdBuilder;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Accessors(chain = true, fluent = true)
public class DownloadFileImpl extends IdentityMappingEndpoint<PNDownloadFileResult> implements DownloadFile {

    private final String channel;
    private final String fileId;
    private final String fileName;

    @Setter
    private String cipherKey;

    public DownloadFileImpl(String channel, String fileId, String fileName, PubNub pubnub) {
        super(pubnub);
        this.channel = channel;
        this.fileId = fileId;
        this.fileName = fileName;
    }

    @Override
    @NotNull
    protected Endpoint<PNDownloadFileResult> createAction() {
        return pubnub.downloadFile(
                channel,
                fileName,
                fileId,
                cipherKey
        );
    }

    public static Builder builder(PubNub pubnub) {
        return new Builder(ChannelFileNameFileIdBuilder.create((channel, fileName, fileId) ->
                new DownloadFileImpl(channel, fileId, fileName, pubnub)));
    }

    public static class Builder extends ChannelFileNameFileIdBuilder<DownloadFile> implements DownloadFile.Builder {
        private Builder(BuilderSteps.ChannelStep<FilesBuilderSteps.FileNameStep<FilesBuilderSteps.FileIdStep<DownloadFile>>> builder) {
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