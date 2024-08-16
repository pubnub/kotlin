package com.pubnub.api.java.endpoints.access;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.UUIDGrant;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;

import java.util.List;

public interface GrantToken extends Endpoint<PNGrantTokenResult> {
    GrantToken ttl(Integer ttl);

    GrantToken meta(Object meta);

    GrantToken authorizedUUID(String authorizedUUID);

    GrantToken channels(List<ChannelGrant> channels);

    GrantToken channelGroups(List<ChannelGroupGrant> channelGroups);

    GrantToken uuids(List<UUIDGrant> uuids);
}
