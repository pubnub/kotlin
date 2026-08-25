package com.pubnub.internal.java.endpoints.datasync.entity;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.entity.SetEntity;
import com.pubnub.api.java.models.consumer.datasync.entity.PNEntityConverter;
import com.pubnub.api.java.models.consumer.datasync.entity.PNSetEntityResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class SetEntityImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.entity.PNSetEntityResult, PNSetEntityResult>
        implements SetEntity {

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

    public SetEntityImpl(String entityId, int entityClassVersion, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.entityId = entityId;
        this.entityClassVersion = entityClassVersion;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNSetEntityResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.entity.PNSetEntityResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNSetEntityResult(
                        result.getStatus(),
                        PNEntityConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.entity.PNSetEntityResult> createRemoteAction() {
        return pubnub.getDataSync().setEntity(
                entityId,
                entityClassVersion,
                status,
                payload,
                ifMatch
        );
    }
}