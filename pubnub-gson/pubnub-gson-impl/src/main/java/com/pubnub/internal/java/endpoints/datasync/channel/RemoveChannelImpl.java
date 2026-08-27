package com.pubnub.internal.java.endpoints.datasync.channel;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.channel.RemoveChannel;
import com.pubnub.api.java.models.consumer.datasync.channel.DataSyncRemoveChannelResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Accessors(chain = true, fluent = true)
public class RemoveChannelImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.channel.DataSyncRemoveChannelResult, DataSyncRemoveChannelResult>
        implements RemoveChannel {

    private final String channelId;

    @Setter
    @Nullable
    private String ifMatch;

    public RemoveChannelImpl(String channelId, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.channelId = channelId;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<DataSyncRemoveChannelResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.channel.DataSyncRemoveChannelResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new DataSyncRemoveChannelResult(result.getStatus())
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.channel.DataSyncRemoveChannelResult> createRemoteAction() {
        return pubnub.getDataSync().removeChannel(channelId, ifMatch);
    }
}