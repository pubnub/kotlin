package com.pubnub.api.endpoints.files;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.files.PNFileUploadResult;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.InputStream;

@Setter
@Accessors(chain = true, fluent = true)
public class SendFile extends Endpoint<PNFileUploadResult> {

    private final String channel;
    private final String fileName;
    private final InputStream inputStream;

    @Setter
    private Object message;
    @Setter
    private Object meta;
    @Setter
    private Integer ttl;
    @Setter
    private Boolean shouldStore;
    @Setter
    private String cipherKey;

    public SendFile(com.pubnub.internal.PubNub pubnub, String channel, String fileName, InputStream inputStream) {
        super(pubnub);
        this.channel = channel;
        this.fileName = fileName;
        this.inputStream = inputStream;
    }

    @Override
    protected ExtendedRemoteAction<PNFileUploadResult> createAction() {
        return pubnub.sendFile(
                channel,
                fileName,
                inputStream,
                message,
                meta,
                ttl,
                shouldStore,
                cipherKey
        );
    }

    public static Builder builder(com.pubnub.internal.PubNub pubnub) {
        return new Builder(pubnub);
    }

    public static class Builder implements BuilderSteps.ChannelStep<FilesBuilderSteps.FileNameStep<FilesBuilderSteps.InputStreamStep<SendFile>>> {

        private final com.pubnub.internal.PubNub pubnub;

        Builder(com.pubnub.internal.PubNub pubnub) {
            this.pubnub = pubnub;
        }

        @Override
        public FilesBuilderSteps.FileNameStep<FilesBuilderSteps.InputStreamStep<SendFile>> channel(String channel) {
            return new InnerBuilder(pubnub).channel(channel);
        }

        public static class InnerBuilder implements
                BuilderSteps.ChannelStep<FilesBuilderSteps.FileNameStep<FilesBuilderSteps.InputStreamStep<SendFile>>>,
                FilesBuilderSteps.FileNameStep<FilesBuilderSteps.InputStreamStep<SendFile>>,
                FilesBuilderSteps.InputStreamStep<SendFile> {
            private final com.pubnub.internal.PubNub pubnub;
            private String channelValue;
            private String fileNameValue;

            private InnerBuilder(com.pubnub.internal.PubNub pubnub) {
                this.pubnub = pubnub;
            }

            @Override
            public FilesBuilderSteps.FileNameStep<FilesBuilderSteps.InputStreamStep<SendFile>> channel(String channel) {
                this.channelValue = channel;
                return this;
            }

            @Override
            public FilesBuilderSteps.InputStreamStep<SendFile> fileName(String fileName) {
                this.fileNameValue = fileName;
                return this;
            }

            @Override
            public SendFile inputStream(InputStream inputStream) {
                return new SendFile(pubnub, channelValue, fileNameValue, inputStream);
            }
        }
    }
}
