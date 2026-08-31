package com.pubnub.internal.java.endpoints.datasync.entity;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.entity.CreateEntity;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncClassLevel;
import com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncCreateEntityResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncEntityConverter;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class CreateEntityImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.entity.PNDataSyncCreateEntityResult, PNDataSyncCreateEntityResult>
        implements CreateEntity {

    private final String className;
    private final int classVersion;

    @Setter
    @Nullable
    private String entityId;

    @Setter
    @Nullable
    private PNDataSyncClassLevel classLevel;

    @Setter
    @Nullable
    private String status;

    @Setter
    @Nullable
    private Map<String, Object> payload;

    public CreateEntityImpl(String className, int classVersion, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.className = className;
        this.classVersion = classVersion;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncCreateEntityResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.entity.PNDataSyncCreateEntityResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncCreateEntityResult(
                        result.getStatus(),
                        PNDataSyncEntityConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.entity.PNDataSyncCreateEntityResult> createRemoteAction() {
        return pubnub.getDataSync().createEntity(
                className,
                classVersion,
                classLevel == null ? null : com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel.valueOf(classLevel.name()),
                entityId,
                status,
                payload
        );
    }
}
