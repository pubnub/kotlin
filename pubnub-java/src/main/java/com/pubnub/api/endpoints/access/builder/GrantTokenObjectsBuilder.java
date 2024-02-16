package com.pubnub.api.endpoints.access.builder;

import com.pubnub.api.endpoints.access.GrantToken;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant;
import com.pubnub.internal.PubNubImpl;

import java.util.List;

public class GrantTokenObjectsBuilder extends AbstractGrantTokenBuilder<GrantTokenObjectsBuilder> {

    public GrantTokenObjectsBuilder(PubNubImpl pubnub, GrantToken grantToken) {
        super(pubnub, grantToken);
    }

    /**
     * @param ttl
     * @return instance of this builder
     * @deprecated Use {@link com.pubnub.api.PubNub#grantToken(Integer)} instead.
     */
    public GrantTokenObjectsBuilder ttl(Integer ttl) {
        grantToken.ttl(ttl);
        return this;
    }

    public GrantTokenObjectsBuilder meta(Object meta) {
        grantToken.meta(meta);
        return this;
    }

    public GrantTokenObjectsBuilder channels(List<ChannelGrant> channels) {
        grantToken.channels(channels);
        return this;
    }

    public GrantTokenObjectsBuilder channelGroups(List<ChannelGroupGrant> channelGroups) {
        grantToken.channelGroups(channelGroups);
        return this;
    }

    public GrantTokenObjectsBuilder uuids(List<UUIDGrant> uuids) {
        grantToken.uuids(uuids);
        return this;
    }

    public GrantTokenObjectsBuilder authorizedUUID(String authorizedUUID) {
        grantToken.authorizedUUID(authorizedUUID);
        return this;
    }
}
