package com.pubnub.api.endpoints.files.requiredparambuilder;

import com.pubnub.api.endpoints.files.requiredparambuilder.BuilderSteps.ChannelStep;
import com.pubnub.api.endpoints.files.requiredparambuilder.BuilderSteps.FileIdStep;
import com.pubnub.api.endpoints.files.requiredparambuilder.BuilderSteps.FileNameStep;
import com.pubnub.api.endpoints.remoteaction.PNFunction3;

public abstract class ChannelFileNameFileIdBuilder<T> implements
        ChannelStep<FileNameStep<FileIdStep<T>>> {
    private final ChannelStep<FileNameStep<FileIdStep<T>>> builder;

    public ChannelFileNameFileIdBuilder(ChannelStep<FileNameStep<FileIdStep<T>>> builder) {

        this.builder = builder;
    }

    public static <T> ChannelStep<FileNameStep<FileIdStep<T>>> create(PNFunction3<String, String, String, T> lastStep) {
        return new InnerBuilder<>(lastStep);
    }

    @Override
    public FileNameStep<FileIdStep<T>> channel(String channel) {
        return builder.channel(channel);
    }

    public static class InnerBuilder<T> implements
            ChannelStep<FileNameStep<FileIdStep<T>>>,
            FileNameStep<FileIdStep<T>>,
            FileIdStep<T> {
        private final PNFunction3<String, String, String, T> lastStep;
        private String channelValue;
        private String fileNameValue;

        private InnerBuilder(PNFunction3<String, String, String, T> lastStep) {
            this.lastStep = lastStep;
        }

        @Override
        public FileNameStep<FileIdStep<T>> channel(String channel) {
            this.channelValue = channel;
            return this;
        }

        @Override
        public FileIdStep<T> fileName(String fileName) {
            this.fileNameValue = fileName;
            return this;
        }

        @Override
        public T fileId(String fileId) {
            return lastStep.invoke(channelValue, fileNameValue, fileId);
        }
    }
}
