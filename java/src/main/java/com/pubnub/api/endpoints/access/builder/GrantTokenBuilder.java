package com.pubnub.api.endpoints.access.builder;

import com.pubnub.api.UserId;
import com.pubnub.api.endpoints.access.GrantToken;
import com.pubnub.api.models.consumer.access_manager.sum.SpacePermissions;
import com.pubnub.api.models.consumer.access_manager.sum.UserPermissions;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant;

import java.util.List;
import java.util.Map;

public class GrantTokenBuilder extends AbstractGrantTokenBuilder<GrantTokenBuilder> {

    public GrantTokenBuilder(GrantToken grantToken) {
        super(grantToken);
    }

    /**
     * @deprecated Use {@link com.pubnub.api.PubNub#grantToken(Integer)} instead.
     * @param ttl
     * @return instance of this builder
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
        return new GrantTokenObjectsBuilder(grantToken).channels(channels);
    }

    public GrantTokenObjectsBuilder channelGroups(List<ChannelGroupGrant> channelGroups) {
        return new GrantTokenObjectsBuilder(grantToken).channelGroups(channelGroups);
    }

    public GrantTokenObjectsBuilder uuids(List<UUIDGrant> uuids) {
        return new GrantTokenObjectsBuilder(grantToken).uuids(uuids);
    }

    public GrantTokenObjectsBuilder authorizedUUID(String authorizedUUID) {
        return new GrantTokenObjectsBuilder(grantToken).authorizedUUID(authorizedUUID);
    }

    public GrantTokenEntitiesBuilder authorizedUserId(UserId userId) {
        return new GrantTokenEntitiesBuilder(grantToken).authorizedUserId(userId);
    }

    public GrantTokenEntitiesBuilder spacesPermissions(List<SpacePermissions> spacesPermissions) {
        return new GrantTokenEntitiesBuilder(grantToken).spacesPermissions(spacesPermissions);
    }

    public GrantTokenEntitiesBuilder usersPermissions(List<UserPermissions> usersPermissions) {
        return new GrantTokenEntitiesBuilder(grantToken).usersPermissions(usersPermissions);
    }

    @Override
    public GrantTokenBuilder queryParam(Map<String, String> queryParam) {
        grantToken.queryParam(queryParam);
        return this;
    }
}
