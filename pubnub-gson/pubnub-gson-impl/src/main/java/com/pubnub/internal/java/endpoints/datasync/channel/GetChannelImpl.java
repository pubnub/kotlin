package com.pubnub.internal.java.endpoints.datasync.channel;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.channel.GetChannel;
import com.pubnub.api.java.models.consumer.datasync.channel.DataSyncChannelConverter;
import com.pubnub.api.java.models.consumer.datasync.channel.DataSyncGetChannelResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import org.jetbrains.annotations.NotNull;

public class GetChannelImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.channel.DataSyncGetChannelResult, DataSyncGetChannelResult>
        implements GetChannel {

    private final String channelId;

    public GetChannelImpl(String channelId, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.channelId = channelId;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<DataSyncGetChannelResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.channel.DataSyncGetChannelResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new DataSyncGetChannelResult(
                        result.getStatus(),
                        DataSyncChannelConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.channel.DataSyncGetChannelResult> createRemoteAction() {
        return pubnub.getDataSync().getChannel(channelId);
    }
}