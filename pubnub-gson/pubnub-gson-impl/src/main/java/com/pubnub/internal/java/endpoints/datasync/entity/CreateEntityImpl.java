package com.pubnub.internal.java.endpoints.datasync.entity;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.entity.CreateEntity;
import com.pubnub.api.java.models.consumer.datasync.entity.PNCreateEntityResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNEntityConverter;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class CreateEntityImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.entity.PNCreateEntityResult, PNCreateEntityResult>
        implements CreateEntity {

    private final String entityClass;
    private final int entityClassVersion;

    @Setter
    @Nullable
    private String entityId;

    @Setter
    @Nullable
    private String status;

    @Setter
    @Nullable
    private Map<String, Object> payload;

    public CreateEntityImpl(String entityClass, int entityClassVersion, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.entityClass = entityClass;
        this.entityClassVersion = entityClassVersion;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNCreateEntityResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.entity.PNCreateEntityResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNCreateEntityResult(
                        result.getStatus(),
                        PNEntityConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.entity.PNCreateEntityResult> createRemoteAction() {
        return pubnub.getDataSync().createEntity(
                entityClass,
                entityClassVersion,
                entityId,
                status,
                payload
        );
    }
}