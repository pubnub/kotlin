package com.pubnub.api.endpoints.files;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.files.requiredparambuilder.ChannelFileNameFileIdBuilder;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.files.PNDownloadFileResult;
import com.pubnub.internal.BasePubNub.PubNubImpl;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class DownloadFile extends Endpoint<PNDownloadFileResult> {

    private final String channel;
    private final String fileId;
    private final String fileName;

    @Setter
    private String cipherKey;

    public DownloadFile(String channel, String fileId, String fileName, PubNubImpl pubnub) {
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

    public static class Builder extends ChannelFileNameFileIdBuilder<DownloadFile> {
        private Builder(BuilderSteps.ChannelStep<FilesBuilderSteps.FileNameStep<FilesBuilderSteps.FileIdStep<DownloadFile>>> builder) {
            super(builder);
        }
    }

    public static Builder builder(PubNubImpl pubnub) {
        return new Builder(ChannelFileNameFileIdBuilder.create((channel, fileName, fileId) ->
                new DownloadFile(channel, fileId, fileName, pubnub)));
    }
}