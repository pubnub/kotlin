package com.pubnub.api.endpoints.files;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.files.PNListFilesResult;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.internal.InternalPubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;


@Accessors(chain = true, fluent = true)
public class ListFiles extends DelegatingEndpoint<PNListFilesResult> {

    private final String channel;

    @Setter
    private Integer limit;
    @Setter
    private PNPage.PNNext next;

    public ListFiles(String channel, InternalPubNubClient pubnub) {
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

    public static class Builder implements BuilderSteps.ChannelStep<ListFiles> {

        private final InternalPubNubClient pubnubInstance;

        public Builder(InternalPubNubClient pubnubInstance) {
            this.pubnubInstance = pubnubInstance;
        }

        @Override
        public ListFiles channel(String channel) {
            return new ListFiles(channel, pubnubInstance);
        }
    }
}