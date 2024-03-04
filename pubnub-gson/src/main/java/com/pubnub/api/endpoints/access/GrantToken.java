package com.pubnub.api.endpoints.access;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;

public interface GrantToken extends Endpoint<PNGrantTokenResult> {
    GrantToken ttl(Integer ttl);

    GrantToken meta(Object meta);

    GrantToken authorizedUUID(String authorizedUUID);

    GrantToken channels(java.util.List<com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant> channels);

    GrantToken channelGroups(java.util.List<com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant> channelGroups);

    GrantToken uuids(java.util.List<com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant> uuids);
}
