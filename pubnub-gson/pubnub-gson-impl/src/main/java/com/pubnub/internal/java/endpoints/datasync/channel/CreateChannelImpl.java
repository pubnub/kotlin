package com.pubnub.internal.java.endpoints.datasync.channel;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.channel.CreateChannel;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncClassLevel;
import com.pubnub.api.java.models.consumer.datasync.channel.DataSyncChannelConverter;
import com.pubnub.api.java.models.consumer.datasync.channel.DataSyncCreateChannelResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class CreateChannelImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.channel.DataSyncCreateChannelResult, DataSyncCreateChannelResult>
        implements CreateChannel {

    private final int classVersion;

    @Setter
    @Nullable
    private String channelId;

    @Setter
    @Nullable
    private String className;

    @Setter
    @Nullable
    private PNDataSyncClassLevel classLevel;

    @Setter
    @Nullable
    private String status;

    @Setter
    @Nullable
    private Map<String, Object> payload;

    public CreateChannelImpl(int classVersion, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.classVersion = classVersion;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<DataSyncCreateChannelResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.channel.DataSyncCreateChannelResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new DataSyncCreateChannelResult(
                        result.getStatus(),
                        DataSyncChannelConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.channel.DataSyncCreateChannelResult> createRemoteAction() {
        return pubnub.getDataSync().createChannel(
                classVersion,
                channelId,
                className,
                classLevel == null ? null : com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel.valueOf(classLevel.name()),
                status,
                payload
        );
    }
}