package com.pubnub.internal.java.endpoints.datasync.entity;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.entity.UpdateEntity;
import com.pubnub.api.java.models.consumer.datasync.entity.DataSyncEntityConverter;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.api.java.models.consumer.datasync.entity.DataSyncUpdateEntityResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@Accessors(chain = true, fluent = true)
public class UpdateEntityImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.entity.DataSyncUpdateEntityResult, DataSyncUpdateEntityResult>
        implements UpdateEntity {

    private final String entityId;
    private final List<PNJsonPatchOperation> operations;

    @Setter
    @Nullable
    private String ifMatch;

    public UpdateEntityImpl(String entityId, List<PNJsonPatchOperation> operations, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.entityId = entityId;
        this.operations = operations;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<DataSyncUpdateEntityResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.entity.DataSyncUpdateEntityResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new DataSyncUpdateEntityResult(
                        result.getStatus(),
                        DataSyncEntityConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.entity.DataSyncUpdateEntityResult> createRemoteAction() {
        final List<com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation> mapped =
                operations.stream()
                        .map(op -> new com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation(
                                op.getOp(),
                                op.getPath(),
                                op.getValue(),
                                op.getFrom()
                        ))
                        .collect(Collectors.toList());
        return pubnub.getDataSync().updateEntity(entityId, mapped, ifMatch);
    }
}
