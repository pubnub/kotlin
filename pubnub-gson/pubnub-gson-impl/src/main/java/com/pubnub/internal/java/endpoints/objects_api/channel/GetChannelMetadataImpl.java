package com.pubnub.internal.java.endpoints.objects_api.channel;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.objects_api.channel.GetChannelMetadata;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadataConverter;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNGetChannelMetadataResult;
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Accessors(chain = true, fluent = true)
public class GetChannelMetadataImpl extends DelegatingEndpoint<PNChannelMetadataResult, PNGetChannelMetadataResult> implements GetChannelMetadata {
    public GetChannelMetadataImpl(String channel, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.channel = channel;
    }

    private final String channel;

    @Setter
    private boolean includeCustom;

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNGetChannelMetadataResult> mapResult(@NotNull ExtendedRemoteAction<PNChannelMetadataResult> action) {
        return new MappingRemoteAction<>(action, (PNChannelMetadataResult pnChannelMetadataResult) ->
                new PNGetChannelMetadataResult(
                        pnChannelMetadataResult.getStatus(),
                        PNChannelMetadataConverter.from(pnChannelMetadataResult.getData()
                        )
                ));
    }

    @Override
    @NotNull
    protected Endpoint<PNChannelMetadataResult> createRemoteAction() {
        return pubnub.getChannelMetadata(channel, includeCustom);
    }

    public static class Builder implements GetChannelMetadata.Builder {
        private final PubNub pubnubInstance;

        public Builder(PubNub pubnubInstance) {
            this.pubnubInstance = pubnubInstance;
        }

        @Override
        public GetChannelMetadata channel(final String channel) {
            return new GetChannelMetadataImpl(channel, pubnubInstance);
        }
    }
}
