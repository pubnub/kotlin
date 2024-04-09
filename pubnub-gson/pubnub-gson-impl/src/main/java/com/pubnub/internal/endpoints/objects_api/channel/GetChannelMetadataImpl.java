package com.pubnub.internal.endpoints.objects_api.channel;

import com.pubnub.api.endpoints.objects_api.channel.GetChannelMetadata;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.models.consumer.objects_api.channel.PNGetChannelMetadataResult;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Accessors(chain = true, fluent = true)
public class GetChannelMetadataImpl extends DelegatingEndpoint<PNChannelMetadataResult, PNGetChannelMetadataResult> implements GetChannelMetadata {
    public GetChannelMetadataImpl(String channel, final PubNubCore pubnubInstance) {
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
                        PNChannelMetadata.from(pnChannelMetadataResult.getData()
                        )
                ));
    }

    @Override
    @NotNull
    protected EndpointInterface<PNChannelMetadataResult> createAction() {
        return pubnub.getChannelMetadata(channel, includeCustom);
    }

    @AllArgsConstructor
    public static class Builder implements GetChannelMetadata.Builder {
        private final PubNubCore pubnubInstance;

        @Override
        public GetChannelMetadata channel(final String channel) {
            return new GetChannelMetadataImpl(channel, pubnubInstance);
        }
    }
}
