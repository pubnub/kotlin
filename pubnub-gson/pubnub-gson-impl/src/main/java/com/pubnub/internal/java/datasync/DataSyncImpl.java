package com.pubnub.internal.java.datasync;

import com.pubnub.api.PubNub;
import com.pubnub.api.java.datasync.DataSync;
import com.pubnub.api.java.datasync.EntityApi;
import com.pubnub.api.java.endpoints.datasync.user.CreateUser;
import com.pubnub.api.java.endpoints.datasync.user.GetUser;
import com.pubnub.api.java.endpoints.datasync.user.GetUsers;
import com.pubnub.api.java.endpoints.datasync.user.PatchUser;
import com.pubnub.api.java.endpoints.datasync.user.RemoveUser;
import com.pubnub.api.java.endpoints.datasync.user.UpdateUser;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.internal.java.endpoints.datasync.user.CreateUserImpl;
import com.pubnub.internal.java.endpoints.datasync.user.GetUserImpl;
import com.pubnub.internal.java.endpoints.datasync.user.GetUsersImpl;
import com.pubnub.internal.java.endpoints.datasync.user.PatchUserImpl;
import com.pubnub.internal.java.endpoints.datasync.user.RemoveUserImpl;
import com.pubnub.internal.java.endpoints.datasync.user.UpdateUserImpl;

import java.util.List;

public class DataSyncImpl implements DataSync {
    private final PubNub pubnubInstance;

    public DataSyncImpl(PubNub pubnubInstance) {
        this.pubnubInstance = pubnubInstance;
    }

    @Override
    public EntityApi entity() {
        return new EntityApiImpl(pubnubInstance);
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
    public PatchUser patchUser(String userId, List<PNJsonPatchOperation> operations) {
        return new PatchUserImpl(userId, operations, pubnubInstance);
    }

    @Override
    public UpdateUser updateUser(String userId, int entityClassVersion) {
        return new UpdateUserImpl(userId, entityClassVersion, pubnubInstance);
    }
}
