package com.pubnub.api.endpoints.files;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.files.PNListFilesResult;
import com.pubnub.api.models.consumer.objects.PNPage;
import lombok.Setter;
import lombok.experimental.Accessors;


@Accessors(chain = true, fluent = true)
public class ListFiles extends ValidatingEndpoint<PNListFilesResult> {

    private final String channel;

    @Setter
    private Integer limit;
    @Setter
    private PNPage.PNNext next;

    public ListFiles(String channel, com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
        this.channel = channel;
    }

    @Override
    protected Endpoint<PNListFilesResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.listFiles(
                channel,
                limit,
                next
        ));
    }

    public static class Builder implements BuilderSteps.ChannelStep<ListFiles> {

        private final com.pubnub.internal.PubNub pubnubInstance;

        public Builder(com.pubnub.internal.PubNub pubnubInstance) {
            this.pubnubInstance = pubnubInstance;
        }

        @Override
        public ListFiles channel(String channel) {
            return new ListFiles(channel, pubnubInstance);
        }
    }
}