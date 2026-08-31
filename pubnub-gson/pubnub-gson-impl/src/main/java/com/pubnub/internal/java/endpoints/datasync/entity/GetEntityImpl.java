package com.pubnub.internal.java.endpoints.datasync.entity;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.entity.GetEntity;
import com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncEntityConverter;
import com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncGetEntityResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import org.jetbrains.annotations.NotNull;

public class GetEntityImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.entity.PNDataSyncGetEntityResult, PNDataSyncGetEntityResult>
        implements GetEntity {

    private final String entityId;

    public GetEntityImpl(String entityId, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.entityId = entityId;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncGetEntityResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.entity.PNDataSyncGetEntityResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncGetEntityResult(
                        result.getStatus(),
                        PNDataSyncEntityConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.entity.PNDataSyncGetEntityResult> createRemoteAction() {
        return pubnub.getDataSync().getEntity(entityId);
    }
}
