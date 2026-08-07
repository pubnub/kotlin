package com.pubnub.internal.java.endpoints.datasync.entity;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.entity.RemoveEntity;
import com.pubnub.api.java.models.consumer.datasync.entity.PNRemoveEntityResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Accessors(chain = true, fluent = true)
public class RemoveEntityImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.entity.PNRemoveEntityResult, PNRemoveEntityResult>
        implements RemoveEntity {

    private final String entityId;

    @Setter
    @Nullable
    private String ifMatch;

    public RemoveEntityImpl(String entityId, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.entityId = entityId;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNRemoveEntityResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.entity.PNRemoveEntityResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNRemoveEntityResult(result.getStatus())
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.entity.PNRemoveEntityResult> createRemoteAction() {
        return pubnub.getDataSync().removeEntity(entityId, ifMatch);
    }
}
