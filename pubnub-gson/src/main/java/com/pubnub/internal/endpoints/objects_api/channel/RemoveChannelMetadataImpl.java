package com.pubnub.internal.endpoints.objects_api.channel;

import com.pubnub.api.endpoints.objects_api.channel.RemoveChannelMetadata;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects_api.channel.PNRemoveChannelMetadataResult;
import com.pubnub.internal.CorePubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.AllArgsConstructor;

public class RemoveChannelMetadataImpl extends DelegatingEndpoint<PNRemoveChannelMetadataResult> implements RemoveChannelMetadata {
    public RemoveChannelMetadataImpl(String channel, final CorePubNubClient pubnubInstance) {
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

    @AllArgsConstructor
    public static class Builder implements RemoveChannelMetadata.Builder {
        private final CorePubNubClient pubnubInstance;

        @Override
        public RemoveChannelMetadata channel(String channel) {
            return new RemoveChannelMetadataImpl(channel, pubnubInstance);
        }
    }
}
