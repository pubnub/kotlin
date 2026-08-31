package com.pubnub.internal.java.endpoints.datasync.channel;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.channel.GetChannel;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncChannelConverter;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncGetChannelResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import org.jetbrains.annotations.NotNull;

public class GetChannelImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.channel.PNDataSyncGetChannelResult, PNDataSyncGetChannelResult>
        implements GetChannel {

    private final String channelId;

    public GetChannelImpl(String channelId, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.channelId = channelId;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncGetChannelResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.channel.PNDataSyncGetChannelResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncGetChannelResult(
                        result.getStatus(),
                        PNDataSyncChannelConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.channel.PNDataSyncGetChannelResult> createRemoteAction() {
        return pubnub.getDataSync().getChannel(channelId);
    }
}