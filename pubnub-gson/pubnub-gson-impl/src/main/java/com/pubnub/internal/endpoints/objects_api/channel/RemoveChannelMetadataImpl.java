package com.pubnub.internal.endpoints.objects_api.channel;

import com.pubnub.api.endpoints.objects_api.channel.RemoveChannelMetadata;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects_api.channel.PNRemoveChannelMetadataResult;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import com.pubnub.internal.models.consumer.objects.PNRemoveMetadataResult;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

public class RemoveChannelMetadataImpl extends DelegatingEndpoint<PNRemoveMetadataResult, PNRemoveChannelMetadataResult> implements RemoveChannelMetadata {
    public RemoveChannelMetadataImpl(String channel, final PubNubCore pubnubInstance) {
        super(pubnubInstance);
        this.channel = channel;
    }

    private final String channel;

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNRemoveChannelMetadataResult> mapResult(@NotNull ExtendedRemoteAction<PNRemoveMetadataResult> action) {
        return new MappingRemoteAction<>(action,
                result -> new PNRemoveChannelMetadataResult(
                        result.getStatus(),
                        null
                )
        );
    }

    @Override
    @NotNull
    protected EndpointInterface<PNRemoveMetadataResult> createAction() {
        return pubnub.removeChannelMetadata(channel);
    }

    @AllArgsConstructor
    public static class Builder implements RemoveChannelMetadata.Builder {
        private final PubNubCore pubnubInstance;

        @Override
        public RemoveChannelMetadata channel(String channel) {
            return new RemoveChannelMetadataImpl(channel, pubnubInstance);
        }
    }
}
