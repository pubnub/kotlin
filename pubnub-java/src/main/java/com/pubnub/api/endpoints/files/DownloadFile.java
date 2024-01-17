package com.pubnub.api.endpoints.files;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.files.requiredparambuilder.ChannelFileNameFileIdBuilder;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.files.PNDownloadFileResult;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class DownloadFile extends ValidatingEndpoint<PNDownloadFileResult> {

    private final String channel;
    private final String fileId;
    private final String fileName;

    @Setter
    private String cipherKey;

    public DownloadFile(String channel, String fileId, String fileName, com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
        this.channel = channel;
        this.fileId = fileId;
        this.fileName = fileName;
    }

    @Override
    protected Endpoint<PNDownloadFileResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.downloadFile(
                channel,
                fileName,
                fileId,
                cipherKey
        ));
    }

    public static class Builder extends ChannelFileNameFileIdBuilder<DownloadFile> {
        private Builder(BuilderSteps.ChannelStep<FilesBuilderSteps.FileNameStep<FilesBuilderSteps.FileIdStep<DownloadFile>>> builder) {
            super(builder);
        }
    }

    public static Builder builder(com.pubnub.internal.PubNub pubnub) {
        return new Builder(ChannelFileNameFileIdBuilder.create((channel, fileName, fileId) ->
                new DownloadFile(channel, fileId, fileName, pubnub)));
    }
}