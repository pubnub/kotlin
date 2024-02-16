package com.pubnub.api.endpoints.access.builder;

import com.pubnub.api.UserId;
import com.pubnub.api.endpoints.access.GrantToken;
import com.pubnub.api.models.consumer.access_manager.sum.SpacePermissions;
import com.pubnub.api.models.consumer.access_manager.sum.UserPermissions;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant;
import com.pubnub.internal.PubNubImpl;

import java.util.List;

public class GrantTokenBuilder extends AbstractGrantTokenBuilder<GrantTokenBuilder> {

    public GrantTokenBuilder(PubNubImpl pubnub, GrantToken grantToken) {
        super(pubnub, grantToken);
    }

    /**
     * @param ttl
     * @return instance of this builder
     * @deprecated Use {@link com.pubnub.api.PubNub#grantToken(Integer)} instead.
     */
    @Deprecated
    public GrantTokenBuilder ttl(Integer ttl) {
        grantToken.ttl(ttl);
        return this;
    }

    public GrantTokenBuilder meta(Object meta) {
        grantToken.meta(meta);
        return this;
    }

    public GrantTokenObjectsBuilder channels(List<ChannelGrant> channels) {
        return new GrantTokenObjectsBuilder(pubnub, grantToken).channels(channels);
    }

    public GrantTokenObjectsBuilder channelGroups(List<ChannelGroupGrant> channelGroups) {
        return new GrantTokenObjectsBuilder(pubnub, grantToken).channelGroups(channelGroups);
    }

    public GrantTokenObjectsBuilder uuids(List<UUIDGrant> uuids) {
        return new GrantTokenObjectsBuilder(pubnub, grantToken).uuids(uuids);
    }

    public GrantTokenObjectsBuilder authorizedUUID(String authorizedUUID) {
        return new GrantTokenObjectsBuilder(pubnub, grantToken).authorizedUUID(authorizedUUID);
    }

    public GrantTokenEntitiesBuilder authorizedUserId(UserId userId) {
        return new GrantTokenEntitiesBuilder(pubnub, grantToken).authorizedUserId(userId);
    }

    public GrantTokenEntitiesBuilder spacesPermissions(List<SpacePermissions> spacesPermissions) {
        return new GrantTokenEntitiesBuilder(pubnub, grantToken).spacesPermissions(spacesPermissions);
    }

    public GrantTokenEntitiesBuilder usersPermissions(List<UserPermissions> usersPermissions) {
        return new GrantTokenEntitiesBuilder(pubnub, grantToken).usersPermissions(usersPermissions);
    }
}
