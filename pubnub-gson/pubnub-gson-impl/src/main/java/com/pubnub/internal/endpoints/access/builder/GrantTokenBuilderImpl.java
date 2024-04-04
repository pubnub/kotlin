package com.pubnub.internal.endpoints.access.builder;

import com.pubnub.api.UserId;
import com.pubnub.api.endpoints.access.GrantToken;
import com.pubnub.api.endpoints.access.builder.GrantTokenBuilder;
import com.pubnub.api.endpoints.access.builder.GrantTokenEntitiesBuilder;
import com.pubnub.api.endpoints.access.builder.GrantTokenObjectsBuilder;
import com.pubnub.api.models.consumer.access_manager.sum.SpacePermissions;
import com.pubnub.api.models.consumer.access_manager.sum.UserPermissions;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant;
import com.pubnub.internal.PubNubCore;

import java.util.List;

public class GrantTokenBuilderImpl extends AbstractGrantTokenBuilderImpl<GrantTokenBuilderImpl>
        implements GrantTokenBuilder {

    public GrantTokenBuilderImpl(PubNubCore pubnub, GrantToken grantToken) {
        super(pubnub, grantToken);
    }

    @Deprecated
    public GrantTokenBuilderImpl ttl(Integer ttl) {
        grantToken.ttl(ttl);
        return this;
    }

    public GrantTokenBuilderImpl meta(Object meta) {
        grantToken.meta(meta);
        return this;
    }

    @Override
    public GrantTokenObjectsBuilder channels(List<ChannelGrant> channels) {
        return new GrantTokenObjectsBuilderImpl(pubnub, grantToken).channels(channels);
    }

    @Override
    public GrantTokenObjectsBuilder channelGroups(List<ChannelGroupGrant> channelGroups) {
        return new GrantTokenObjectsBuilderImpl(pubnub, grantToken).channelGroups(channelGroups);
    }

    @Override
    public GrantTokenObjectsBuilder uuids(List<UUIDGrant> uuids) {
        return new GrantTokenObjectsBuilderImpl(pubnub, grantToken).uuids(uuids);
    }

    @Override
    public GrantTokenObjectsBuilder authorizedUUID(String authorizedUUID) {
        return new GrantTokenObjectsBuilderImpl(pubnub, grantToken).authorizedUUID(authorizedUUID);
    }

    @Override
    public GrantTokenEntitiesBuilder authorizedUserId(UserId userId) {
        return new GrantTokenEntitiesBuilderImpl(pubnub, grantToken).authorizedUserId(userId);
    }

    @Override
    public GrantTokenEntitiesBuilder spacesPermissions(List<SpacePermissions> spacesPermissions) {
        return new GrantTokenEntitiesBuilderImpl(pubnub, grantToken).spacesPermissions(spacesPermissions);
    }

    @Override
    public GrantTokenEntitiesBuilder usersPermissions(List<UserPermissions> usersPermissions) {
        return new GrantTokenEntitiesBuilderImpl(pubnub, grantToken).usersPermissions(usersPermissions);
    }
}
