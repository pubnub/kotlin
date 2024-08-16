package com.pubnub.internal.java.endpoints.objects_api.channel;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.objects_api.channel.RemoveChannelMetadata;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNRemoveChannelMetadataResult;
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import org.jetbrains.annotations.NotNull;

public class RemoveChannelMetadataImpl extends DelegatingEndpoint<PNRemoveMetadataResult, PNRemoveChannelMetadataResult> implements RemoveChannelMetadata {
    public RemoveChannelMetadataImpl(String channel, final PubNub pubnubInstance) {
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
    protected Endpoint<PNRemoveMetadataResult> createAction() {
        return pubnub.removeChannelMetadata(channel);
    }

    public static class Builder implements RemoveChannelMetadata.Builder {
        private final PubNub pubnubInstance;

        public Builder(PubNub pubnubInstance) {
            this.pubnubInstance = pubnubInstance;
        }

        @Override
        public RemoveChannelMetadata channel(String channel) {
            return new RemoveChannelMetadataImpl(channel, pubnubInstance);
        }
    }
}
