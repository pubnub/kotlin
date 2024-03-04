package com.pubnub.internal.endpoints.files;

import com.pubnub.api.endpoints.files.ListFiles;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.files.PNListFilesResult;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class ListFilesImpl extends DelegatingEndpoint<PNListFilesResult> implements ListFiles {

    private final String channel;

    @Setter
    private Integer limit;
    @Setter
    private PNPage.PNNext next;

    public ListFilesImpl(String channel, PubNubCore pubnub) {
        super(pubnub);
        this.channel = channel;
    }

    @Override
    protected ExtendedRemoteAction<PNListFilesResult> createAction() {
        return pubnub.listFiles(
                channel,
                limit,
                next
        );
    }

    public static class Builder implements ListFiles.Builder {

        private final PubNubCore pubnubInstance;

        public Builder(PubNubCore pubnubInstance) {
            this.pubnubInstance = pubnubInstance;
        }

        @Override
        public ListFiles channel(String channel) {
            return new ListFilesImpl(channel, pubnubInstance);
        }
    }
}