package com.pubnub.api.endpoints.objects_api.channel;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects_api.channel.PNRemoveChannelMetadataResult;
import com.pubnub.internal.InternalPubNubClient;
import lombok.AllArgsConstructor;

public class RemoveChannelMetadata extends Endpoint<PNRemoveChannelMetadataResult> {
    public RemoveChannelMetadata(String channel, final InternalPubNubClient pubnubInstance) {
        super(pubnubInstance);
        this.channel = channel;
    }

    private final String channel;

    @Override
    protected ExtendedRemoteAction<PNRemoveChannelMetadataResult> createAction() {
        return new MappingRemoteAction<>(
                pubnub.removeChannelMetadata(channel),
                result -> new PNRemoveChannelMetadataResult(
                        result.getStatus(),
                        null
                )
        );
    }

    public static Builder builder(final InternalPubNubClient pubnubInstance) {
        return new Builder(pubnubInstance);
    }

    @AllArgsConstructor
    public static class Builder implements BuilderSteps.ChannelStep<RemoveChannelMetadata> {
        private final InternalPubNubClient pubnubInstance;

        @Override
        public RemoveChannelMetadata channel(final String channel) {
            return new RemoveChannelMetadata(channel, pubnubInstance);
        }
    }
}
