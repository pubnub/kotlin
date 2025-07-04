package com.pubnub.api.java.endpoints.access.builder;

import com.pubnub.api.UserId;
import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.access_manager.sum.SpacePermissions;
import com.pubnub.api.java.models.consumer.access_manager.sum.UserPermissions;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;
import com.pubnub.api.java.models.consumer.access_manager.v3.UUIDGrant;

import java.util.List;

public interface GrantTokenBuilder extends Endpoint<PNGrantTokenResult> {
    /**
     * @param ttl
     * @return instance of this builder
     * @deprecated Use {@link com.pubnub.api.java.PubNub#grantToken(int)} instead.
     */
    @Deprecated
    GrantTokenBuilder ttl(Integer ttl);

    GrantTokenBuilder meta(Object meta);

    GrantTokenObjectsBuilder channels(List<ChannelGrant> channels);

    GrantTokenObjectsBuilder channelGroups(List<ChannelGroupGrant> channelGroups);

    GrantTokenObjectsBuilder uuids(List<UUIDGrant> uuids);

    GrantTokenObjectsBuilder authorizedUUID(String authorizedUUID);

    @Deprecated
    GrantTokenEntitiesBuilder authorizedUserId(UserId userId);

    @Deprecated
    GrantTokenEntitiesBuilder spacesPermissions(List<SpacePermissions> spacesPermissions);

    @Deprecated
    GrantTokenEntitiesBuilder usersPermissions(List<UserPermissions> usersPermissions);
}
