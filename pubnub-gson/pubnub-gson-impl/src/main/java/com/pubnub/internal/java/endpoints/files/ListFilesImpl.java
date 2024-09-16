package com.pubnub.internal.java.endpoints.files;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.endpoints.files.ListFiles;
import com.pubnub.api.models.consumer.files.PNListFilesResult;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.internal.java.endpoints.PassthroughEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Accessors(chain = true, fluent = true)
public class ListFilesImpl extends PassthroughEndpoint<PNListFilesResult> implements ListFiles {

    private final String channel;

    @Setter
    private Integer limit;
    @Setter
    private PNPage.PNNext next;

    public ListFilesImpl(String channel, PubNub pubnub) {
        super(pubnub);
        this.channel = channel;
    }

    @Override
    @NotNull
    protected Endpoint<PNListFilesResult> createRemoteAction() {
        return pubnub.listFiles(
                channel,
                limit,
                next
        );
    }

    public static class Builder implements ListFiles.Builder {

        private final PubNub pubnubInstance;

        public Builder(PubNub pubnubInstance) {
            this.pubnubInstance = pubnubInstance;
        }

        @Override
        public ListFiles channel(String channel) {
            return new ListFilesImpl(channel, pubnubInstance);
        }
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channel == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING);
        }

        if (limit != null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_INVALID_ARGUMENTS,
                    "Limit should be in range from 1 to 100 (both inclusive)");
        }

        if (next != null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_INVALID_ARGUMENTS,
                    "Next should not be an empty string");
        }
    }
}