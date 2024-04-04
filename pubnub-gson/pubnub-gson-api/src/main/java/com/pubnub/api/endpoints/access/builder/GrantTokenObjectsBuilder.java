package com.pubnub.api.endpoints.access.builder;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant;

import java.util.List;

public interface GrantTokenObjectsBuilder extends Endpoint<PNGrantTokenResult> {

    /**
     * @param ttl
     * @return instance of this builder
     * @deprecated Use {@link com.pubnub.api.PubNub#grantToken(int)}} instead.
     */
    GrantTokenObjectsBuilder ttl(Integer ttl);

    GrantTokenObjectsBuilder meta(Object meta);

    GrantTokenObjectsBuilder channels(List<ChannelGrant> channels);

    GrantTokenObjectsBuilder channelGroups(List<ChannelGroupGrant> channelGroups);

    GrantTokenObjectsBuilder uuids(List<UUIDGrant> uuids);

    GrantTokenObjectsBuilder authorizedUUID(String authorizedUUID);
}
