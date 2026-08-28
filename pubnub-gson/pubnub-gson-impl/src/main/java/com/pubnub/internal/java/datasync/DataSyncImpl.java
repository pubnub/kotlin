package com.pubnub.internal.java.datasync;

import com.pubnub.api.PubNub;
import com.pubnub.api.java.datasync.DataSync;
import com.pubnub.api.java.endpoints.datasync.channel.CreateChannel;
import com.pubnub.api.java.endpoints.datasync.channel.GetChannel;
import com.pubnub.api.java.endpoints.datasync.channel.GetChannels;
import com.pubnub.api.java.endpoints.datasync.channel.RemoveChannel;
import com.pubnub.api.java.endpoints.datasync.channel.SetChannel;
import com.pubnub.api.java.endpoints.datasync.channel.UpdateChannel;
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
import com.pubnub.internal.java.endpoints.datasync.channel.CreateChannelImpl;
import com.pubnub.internal.java.endpoints.datasync.channel.GetChannelImpl;
import com.pubnub.internal.java.endpoints.datasync.channel.GetChannelsImpl;
import com.pubnub.internal.java.endpoints.datasync.channel.RemoveChannelImpl;
import com.pubnub.internal.java.endpoints.datasync.channel.SetChannelImpl;
import com.pubnub.internal.java.endpoints.datasync.channel.UpdateChannelImpl;
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
    public CreateEntity createEntity(String className, int classVersion) {
        return new CreateEntityImpl(className, classVersion, pubnubInstance);
    }

    @Override
    public RemoveEntity removeEntity(String entityId) {
        return new RemoveEntityImpl(entityId, pubnubInstance);
    }

    @Override
    public GetEntities getEntities(String className) {
        return new GetEntitiesImpl(className, pubnubInstance);
    }

    @Override
    public UpdateEntity updateEntity(String entityId, List<PNJsonPatchOperation> operations) {
        return new UpdateEntityImpl(entityId, operations, pubnubInstance);
    }

    @Override
    public SetEntity setEntity(String entityId, int classVersion) {
        return new SetEntityImpl(entityId, classVersion, pubnubInstance);
    }

    @Override
    public GetUser getUser(String userId) {
        return new GetUserImpl(userId, pubnubInstance);
    }

    @Override
    public CreateUser createUser(int classVersion) {
        return new CreateUserImpl(classVersion, pubnubInstance);
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
    public SetUser setUser(String userId, int classVersion) {
        return new SetUserImpl(userId, classVersion, pubnubInstance);
    }

    @Override
    public GetChannel getChannel(String channelId) {
        return new GetChannelImpl(channelId, pubnubInstance);
    }

    @Override
    public CreateChannel createChannel(int classVersion) {
        return new CreateChannelImpl(classVersion, pubnubInstance);
    }

    @Override
    public RemoveChannel removeChannel(String channelId) {
        return new RemoveChannelImpl(channelId, pubnubInstance);
    }

    @Override
    public GetChannels getChannels() {
        return new GetChannelsImpl(pubnubInstance);
    }

    @Override
    public UpdateChannel updateChannel(String channelId, List<PNJsonPatchOperation> operations) {
        return new UpdateChannelImpl(channelId, operations, pubnubInstance);
    }

    @Override
    public SetChannel setChannel(String channelId, int classVersion) {
        return new SetChannelImpl(channelId, classVersion, pubnubInstance);
    }
}
