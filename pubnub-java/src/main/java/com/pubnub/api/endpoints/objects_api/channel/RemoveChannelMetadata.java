package com.pubnub.api.endpoints.objects_api.channel;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects_api.channel.PNRemoveChannelMetadataResult;
import lombok.AllArgsConstructor;

public class RemoveChannelMetadata extends Endpoint<PNRemoveChannelMetadataResult> {
    public RemoveChannelMetadata(String channel, final com.pubnub.internal.PubNub pubnubInstance) {
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

    public static Builder builder(final com.pubnub.internal.PubNub pubnubInstance) {
        return new Builder(pubnubInstance);
    }

    @AllArgsConstructor
    public static class Builder implements BuilderSteps.ChannelStep<RemoveChannelMetadata> {
        private final com.pubnub.internal.PubNub pubnubInstance;

        @Override
        public RemoveChannelMetadata channel(final String channel) {
            return new RemoveChannelMetadata(channel, pubnubInstance);
        }
    }
}
