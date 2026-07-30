package com.pubnub.internal.java.datasync;

import com.pubnub.api.PubNub;
import com.pubnub.api.java.datasync.EntityApi;
import com.pubnub.api.java.endpoints.datasync.entity.CreateEntity;
import com.pubnub.api.java.endpoints.datasync.entity.GetEntities;
import com.pubnub.api.java.endpoints.datasync.entity.GetEntity;
import com.pubnub.api.java.endpoints.datasync.entity.PatchEntity;
import com.pubnub.api.java.endpoints.datasync.entity.RemoveEntity;
import com.pubnub.api.java.endpoints.datasync.entity.UpdateEntity;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.internal.java.endpoints.datasync.entity.CreateEntityImpl;
import com.pubnub.internal.java.endpoints.datasync.entity.GetEntitiesImpl;
import com.pubnub.internal.java.endpoints.datasync.entity.GetEntityImpl;
import com.pubnub.internal.java.endpoints.datasync.entity.PatchEntityImpl;
import com.pubnub.internal.java.endpoints.datasync.entity.RemoveEntityImpl;
import com.pubnub.internal.java.endpoints.datasync.entity.UpdateEntityImpl;

import java.util.List;

public class EntityApiImpl implements EntityApi {
    private final PubNub pubnubInstance;

    public EntityApiImpl(PubNub pubnubInstance) {
        this.pubnubInstance = pubnubInstance;
    }

    @Override
    public GetEntity get(String entityId) {
        return new GetEntityImpl(entityId, pubnubInstance);
    }

    @Override
    public CreateEntity create(String entityClass, int entityClassVersion) {
        return new CreateEntityImpl(entityClass, entityClassVersion, pubnubInstance);
    }

    @Override
    public RemoveEntity delete(String entityId) {
        return new RemoveEntityImpl(entityId, pubnubInstance);
    }

    @Override
    public GetEntities getAll(String entityClass) {
        return new GetEntitiesImpl(entityClass, pubnubInstance);
    }

    @Override
    public PatchEntity patch(String entityId, List<PNJsonPatchOperation> operations) {
        return new PatchEntityImpl(entityId, operations, pubnubInstance);
    }

    @Override
    public UpdateEntity update(String entityId, int entityClassVersion) {
        return new UpdateEntityImpl(entityId, entityClassVersion, pubnubInstance);
    }
}
