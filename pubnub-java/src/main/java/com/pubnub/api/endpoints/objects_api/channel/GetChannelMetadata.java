package com.pubnub.api.endpoints.objects_api.channel;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.models.consumer.objects_api.channel.PNGetChannelMetadataResult;
import com.pubnub.internal.InternalPubNubClient;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class GetChannelMetadata extends DelegatingEndpoint<PNGetChannelMetadataResult> {
    public GetChannelMetadata(String channel, final InternalPubNubClient pubnubInstance) {
        super(pubnubInstance);
        this.channel = channel;
    }

    private final String channel;

    @Setter
    private boolean includeCustom;

    @Override
    protected ExtendedRemoteAction<PNGetChannelMetadataResult> createAction() {
        return new MappingRemoteAction<>(pubnub.getChannelMetadata(channel, includeCustom),
                pnChannelMetadataResult ->
                        new PNGetChannelMetadataResult(
                                pnChannelMetadataResult.getStatus(),
                                PNChannelMetadata.from(pnChannelMetadataResult.getData()
                                )
                        )
        );
    }

    public static Builder builder(final InternalPubNubClient pubnubInstance) {
        return new Builder(pubnubInstance);
    }

    @AllArgsConstructor
    public static class Builder implements BuilderSteps.ChannelStep<GetChannelMetadata> {
        private final InternalPubNubClient pubnubInstance;

        @Override
        public GetChannelMetadata channel(final String channel) {
            return new GetChannelMetadata(channel, pubnubInstance);
        }
    }
}
