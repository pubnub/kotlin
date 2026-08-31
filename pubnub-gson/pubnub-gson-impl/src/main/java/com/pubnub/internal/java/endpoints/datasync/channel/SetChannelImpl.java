package com.pubnub.internal.java.endpoints.datasync.channel;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.channel.SetChannel;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncChannelConverter;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncSetChannelResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class SetChannelImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.channel.PNDataSyncSetChannelResult, PNDataSyncSetChannelResult>
        implements SetChannel {

    private final String channelId;
    private final int classVersion;

    @Setter
    @Nullable
    private String status;

    @Setter
    @Nullable
    private Map<String, Object> payload;

    @Setter
    @Nullable
    private String ifMatch;

    public SetChannelImpl(String channelId, int classVersion, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.channelId = channelId;
        this.classVersion = classVersion;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncSetChannelResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.channel.PNDataSyncSetChannelResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncSetChannelResult(
                        result.getStatus(),
                        PNDataSyncChannelConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.channel.PNDataSyncSetChannelResult> createRemoteAction() {
        return pubnub.getDataSync().setChannel(
                channelId,
                classVersion,
                status,
                payload,
                ifMatch
        );
    }
}