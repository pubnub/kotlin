package com.pubnub.internal.java.datasync;

import com.pubnub.api.PubNub;
import com.pubnub.api.java.datasync.UserApi;
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

public class UserApiImpl implements UserApi {
    private final PubNub pubnubInstance;

    public UserApiImpl(PubNub pubnubInstance) {
        this.pubnubInstance = pubnubInstance;
    }

    @Override
    public GetUser get(String userId) {
        return new GetUserImpl(userId, pubnubInstance);
    }

    @Override
    public CreateUser create(int entityClassVersion) {
        return new CreateUserImpl(entityClassVersion, pubnubInstance);
    }

    @Override
    public RemoveUser delete(String userId) {
        return new RemoveUserImpl(userId, pubnubInstance);
    }

    @Override
    public GetUsers getAll() {
        return new GetUsersImpl(pubnubInstance);
    }

    @Override
    public PatchUser patch(String userId, List<PNJsonPatchOperation> operations) {
        return new PatchUserImpl(userId, operations, pubnubInstance);
    }

    @Override
    public UpdateUser update(String userId, int entityClassVersion) {
        return new UpdateUserImpl(userId, entityClassVersion, pubnubInstance);
    }
}
