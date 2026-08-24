package com.pubnub.internal.java.endpoints.datasync.entity;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.entity.UpdateEntity;
import com.pubnub.api.java.models.consumer.datasync.entity.PNEntityConverter;
import com.pubnub.api.java.models.consumer.datasync.entity.PNUpdateEntityResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class UpdateEntityImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.entity.PNUpdateEntityResult, PNUpdateEntityResult>
        implements UpdateEntity {

    private final String entityId;
    private final int entityClassVersion;

    @Setter
    @Nullable
    private String status;

    @Setter
    @Nullable
    private Map<String, Object> payload;

    @Setter
    @Nullable
    private String ifMatch;

    public UpdateEntityImpl(String entityId, int entityClassVersion, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.entityId = entityId;
        this.entityClassVersion = entityClassVersion;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNUpdateEntityResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.entity.PNUpdateEntityResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNUpdateEntityResult(
                        result.getStatus(),
                        PNEntityConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.entity.PNUpdateEntityResult> createRemoteAction() {
        return pubnub.getDataSync().updateEntity(
                entityId,
                entityClassVersion,
                status,
                payload,
                ifMatch
        );
    }
}