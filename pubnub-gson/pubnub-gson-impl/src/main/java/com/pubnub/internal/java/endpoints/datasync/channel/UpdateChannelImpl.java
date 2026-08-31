package com.pubnub.internal.java.endpoints.datasync.channel;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.channel.UpdateChannel;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncChannelConverter;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncUpdateChannelResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@Accessors(chain = true, fluent = true)
public class UpdateChannelImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.channel.PNDataSyncUpdateChannelResult, PNDataSyncUpdateChannelResult>
        implements UpdateChannel {

    private final String channelId;
    private final List<PNJsonPatchOperation> operations;

    @Setter
    @Nullable
    private String ifMatch;

    public UpdateChannelImpl(String channelId, List<PNJsonPatchOperation> operations, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.channelId = channelId;
        this.operations = operations;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncUpdateChannelResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.channel.PNDataSyncUpdateChannelResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncUpdateChannelResult(
                        result.getStatus(),
                        PNDataSyncChannelConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.channel.PNDataSyncUpdateChannelResult> createRemoteAction() {
        final List<com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation> mapped =
                operations.stream()
                        .map(op -> new com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation(
                                op.getOp(),
                                op.getPath(),
                                op.getValue(),
                                op.getFrom()
                        ))
                        .collect(Collectors.toList());
        return pubnub.getDataSync().updateChannel(channelId, mapped, ifMatch);
    }
}