package com.pubnub.api.java.endpoints.access.builder;

import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.UUIDGrant;

import java.util.List;

/**
 * Legacy (App Context v2 UUID) grant path. Adds the {@code uuids(...)} bucket and the {@code authorizedUUID(...)}
 * principal on top of the shared {@link AbstractGrantTokenBuilder} setters. The modern flat-list {@code grants(...)}
 * path is intentionally absent here — it stays on the neutral {@link GrantTokenBuilder}, and the two cannot be
 * combined.
 */
public interface GrantTokenObjectsBuilder extends AbstractGrantTokenBuilder {

    @Override
    GrantTokenObjectsBuilder ttl(Integer ttl);

    @Override
    GrantTokenObjectsBuilder meta(Object meta);

    @Override
    GrantTokenObjectsBuilder channels(List<ChannelGrant> channels);

    @Override
    GrantTokenObjectsBuilder channelGroups(List<ChannelGroupGrant> channelGroups);

    /**
     * @see GrantTokenBuilder#getAllChannels(boolean)
     */
    GrantTokenObjectsBuilder getAllChannels(boolean getAllChannels);

    /**
     * @see GrantTokenBuilder#getAllUUIDs(boolean)
     */
    GrantTokenObjectsBuilder getAllUUIDs(boolean getAllUUIDs);

    GrantTokenObjectsBuilder uuids(List<UUIDGrant> uuids);

    GrantTokenObjectsBuilder authorizedUUID(String authorizedUUID);
}