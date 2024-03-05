package com.pubnub.internal.endpoints.files;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.files.DownloadFile;
import com.pubnub.internal.endpoints.files.requiredparambuilder.ChannelFileNameFileIdBuilder;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.files.PNDownloadFileResult;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class DownloadFileImpl extends DelegatingEndpoint<PNDownloadFileResult> implements DownloadFile {

    private final String channel;
    private final String fileId;
    private final String fileName;

    @Setter
    private String cipherKey;

    public DownloadFileImpl(String channel, String fileId, String fileName, PubNubCore pubnub) {
        super(pubnub);
        this.channel = channel;
        this.fileId = fileId;
        this.fileName = fileName;
    }

    @Override
    protected ExtendedRemoteAction<PNDownloadFileResult> createAction() {
        return pubnub.downloadFile(
                channel,
                fileName,
                fileId,
                cipherKey
        );
    }

    public static DownloadFileImpl.Builder builder(PubNubCore pubnub) {
        return new DownloadFileImpl.Builder(ChannelFileNameFileIdBuilder.create((channel, fileName, fileId) ->
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