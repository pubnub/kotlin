package com.pubnub.internal.endpoints.access.builder;

import com.pubnub.api.endpoints.access.GrantToken;
import com.pubnub.api.endpoints.access.builder.GrantTokenObjectsBuilder;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant;
import com.pubnub.internal.PubNubCore;

import java.util.List;

public class GrantTokenObjectsBuilderImpl extends AbstractGrantTokenBuilderImpl<GrantTokenObjectsBuilderImpl>
        implements GrantTokenObjectsBuilder {

    public GrantTokenObjectsBuilderImpl(PubNubCore pubnub, GrantToken grantToken) {
        super(pubnub, grantToken);
    }

    @Override
    public GrantTokenObjectsBuilder ttl(Integer ttl) {
        grantToken.ttl(ttl);
        return this;
    }

    @Override
    public GrantTokenObjectsBuilder meta(Object meta) {
        grantToken.meta(meta);
        return this;
    }

    @Override
    public GrantTokenObjectsBuilder channels(List<ChannelGrant> channels) {
        grantToken.channels(channels);
        return this;
    }

    @Override
    public GrantTokenObjectsBuilder channelGroups(List<ChannelGroupGrant> channelGroups) {
        grantToken.channelGroups(channelGroups);
        return this;
    }

    @Override
    public GrantTokenObjectsBuilder uuids(List<UUIDGrant> uuids) {
        grantToken.uuids(uuids);
        return this;
    }

    @Override
    public GrantTokenObjectsBuilder authorizedUUID(String authorizedUUID) {
        grantToken.authorizedUUID(authorizedUUID);
        return this;
    }
}
