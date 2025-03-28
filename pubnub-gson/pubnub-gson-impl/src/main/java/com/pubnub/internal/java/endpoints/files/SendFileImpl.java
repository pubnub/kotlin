package com.pubnub.internal.java.endpoints.files;

import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.java.endpoints.BuilderSteps;
import com.pubnub.api.java.endpoints.files.SendFile;
import com.pubnub.api.java.endpoints.files.requiredparambuilder.FilesBuilderSteps;
import com.pubnub.api.models.consumer.files.PNFileUploadResult;
import com.pubnub.internal.java.endpoints.PassthroughRemoteAction;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

@Setter
@Accessors(chain = true, fluent = true)
public class SendFileImpl extends PassthroughRemoteAction<PNFileUploadResult> implements SendFile {

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
    @Setter
    private String customMessageType;

    public SendFileImpl(PubNub pubnub, String channel, String fileName, InputStream inputStream) {
        super(pubnub);
        this.channel = channel;
        this.fileName = fileName;
        this.inputStream = inputStream;
    }

    @Override
    @NotNull
    protected ExtendedRemoteAction<PNFileUploadResult> createRemoteAction() {
        return pubnub.sendFile(
                channel,
                fileName,
                inputStream,
                message,
                meta,
                ttl,
                shouldStore,
                cipherKey,
                customMessageType
        );
    }

    public static class Builder implements SendFile.Builder {

        private final PubNub pubnub;

        public Builder(PubNub pubnub) {
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
            private final PubNub pubnub;
            private String channelValue;
            private String fileNameValue;

            private InnerBuilder(PubNub pubnub) {
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
                return new SendFileImpl(pubnub, channelValue, fileNameValue, inputStream);
            }
        }
    }
}
