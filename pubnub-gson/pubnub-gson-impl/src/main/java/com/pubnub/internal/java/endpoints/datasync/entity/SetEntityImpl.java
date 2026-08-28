package com.pubnub.internal.java.endpoints.datasync.entity;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.entity.SetEntity;
import com.pubnub.api.java.models.consumer.datasync.entity.DataSyncEntityConverter;
import com.pubnub.api.java.models.consumer.datasync.entity.DataSyncSetEntityResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class SetEntityImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.entity.DataSyncSetEntityResult, DataSyncSetEntityResult>
        implements SetEntity {

    private final String entityId;
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

    public SetEntityImpl(String entityId, int classVersion, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.entityId = entityId;
        this.classVersion = classVersion;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<DataSyncSetEntityResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.entity.DataSyncSetEntityResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new DataSyncSetEntityResult(
                        result.getStatus(),
                        DataSyncEntityConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.entity.DataSyncSetEntityResult> createRemoteAction() {
        return pubnub.getDataSync().setEntity(
                entityId,
                classVersion,
                status,
                payload,
                ifMatch
        );
    }
}