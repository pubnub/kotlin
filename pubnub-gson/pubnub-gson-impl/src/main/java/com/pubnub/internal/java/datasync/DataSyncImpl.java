package com.pubnub.internal.java.datasync;

import com.pubnub.api.PubNub;
import com.pubnub.api.java.datasync.DataSync;
import com.pubnub.api.java.endpoints.datasync.entity.CreateEntity;
import com.pubnub.api.java.endpoints.datasync.entity.GetEntities;
import com.pubnub.api.java.endpoints.datasync.entity.GetEntity;
import com.pubnub.api.java.endpoints.datasync.entity.UpdateEntity;
import com.pubnub.api.java.endpoints.datasync.entity.RemoveEntity;
import com.pubnub.api.java.endpoints.datasync.entity.SetEntity;
import com.pubnub.api.java.endpoints.datasync.user.CreateUser;
import com.pubnub.api.java.endpoints.datasync.user.GetUser;
import com.pubnub.api.java.endpoints.datasync.user.GetUsers;
import com.pubnub.api.java.endpoints.datasync.user.UpdateUser;
import com.pubnub.api.java.endpoints.datasync.user.RemoveUser;
import com.pubnub.api.java.endpoints.datasync.user.SetUser;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.internal.java.endpoints.datasync.entity.CreateEntityImpl;
import com.pubnub.internal.java.endpoints.datasync.entity.GetEntitiesImpl;
import com.pubnub.internal.java.endpoints.datasync.entity.GetEntityImpl;
import com.pubnub.internal.java.endpoints.datasync.entity.UpdateEntityImpl;
import com.pubnub.internal.java.endpoints.datasync.entity.RemoveEntityImpl;
import com.pubnub.internal.java.endpoints.datasync.entity.SetEntityImpl;
import com.pubnub.internal.java.endpoints.datasync.user.CreateUserImpl;
import com.pubnub.internal.java.endpoints.datasync.user.GetUserImpl;
import com.pubnub.internal.java.endpoints.datasync.user.GetUsersImpl;
import com.pubnub.internal.java.endpoints.datasync.user.UpdateUserImpl;
import com.pubnub.internal.java.endpoints.datasync.user.RemoveUserImpl;
import com.pubnub.internal.java.endpoints.datasync.user.SetUserImpl;

import java.util.List;

public class DataSyncImpl implements DataSync {
    private final PubNub pubnubInstance;

    public DataSyncImpl(PubNub pubnubInstance) {
        this.pubnubInstance = pubnubInstance;
    }

    @Override
    public GetEntity getEntity(String entityId) {
        return new GetEntityImpl(entityId, pubnubInstance);
    }

    @Override
    public CreateEntity createEntity(String entityClass, int entityClassVersion) {
        return new CreateEntityImpl(entityClass, entityClassVersion, pubnubInstance);
    }

    @Override
    public RemoveEntity removeEntity(String entityId) {
        return new RemoveEntityImpl(entityId, pubnubInstance);
    }

    @Override
    public GetEntities getEntities(String entityClass) {
        return new GetEntitiesImpl(entityClass, pubnubInstance);
    }

    @Override
    public UpdateEntity updateEntity(String entityId, List<PNJsonPatchOperation> operations) {
        return new UpdateEntityImpl(entityId, operations, pubnubInstance);
    }

    @Override
    public SetEntity setEntity(String entityId, int entityClassVersion) {
        return new SetEntityImpl(entityId, entityClassVersion, pubnubInstance);
    }

    @Override
    public GetUser getUser(String userId) {
        return new GetUserImpl(userId, pubnubInstance);
    }

    @Override
    public CreateUser createUser(int entityClassVersion) {
        return new CreateUserImpl(entityClassVersion, pubnubInstance);
    }

    @Override
    public RemoveUser removeUser(String userId) {
        return new RemoveUserImpl(userId, pubnubInstance);
    }

    @Override
    public GetUsers getUsers() {
        return new GetUsersImpl(pubnubInstance);
    }

    @Override
    public UpdateUser updateUser(String userId, List<PNJsonPatchOperation> operations) {
        return new UpdateUserImpl(userId, operations, pubnubInstance);
    }

    @Override
    public SetUser setUser(String userId, int entityClassVersion) {
        return new SetUserImpl(userId, entityClassVersion, pubnubInstance);
    }
}
