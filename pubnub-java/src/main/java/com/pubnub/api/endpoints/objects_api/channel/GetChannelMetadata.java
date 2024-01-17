package com.pubnub.api.endpoints.objects_api.channel;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.MappingEndpoint;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.models.consumer.objects_api.channel.PNGetChannelMetadataResult;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class GetChannelMetadata extends ValidatingEndpoint<PNGetChannelMetadataResult> {
    public GetChannelMetadata(String channel, final com.pubnub.internal.PubNub pubnubInstance) {
        super(pubnubInstance);
        this.channel = channel;
    }

    private final String channel;

    @Setter
    private boolean includeCustom;

    @Override
    protected Endpoint<PNGetChannelMetadataResult> createAction() {
        return new MappingEndpoint<>(pubnub.getChannelMetadata(channel, includeCustom),
                pnChannelMetadataResult ->
                        new PNGetChannelMetadataResult(
                                pnChannelMetadataResult.getStatus(),
                                PNChannelMetadata.from(pnChannelMetadataResult.getData()
                                )
                        )
        );
    }

    public static Builder builder(final com.pubnub.internal.PubNub pubnubInstance) {
        return new Builder(pubnubInstance);
    }

    @AllArgsConstructor
    public static class Builder implements BuilderSteps.ChannelStep<GetChannelMetadata> {
        private final com.pubnub.internal.PubNub pubnubInstance;

        @Override
        public GetChannelMetadata channel(final String channel) {
            return new GetChannelMetadata(channel, pubnubInstance);
        }
    }
}
