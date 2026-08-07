package com.pubnub.api.java.endpoints.access.builder;

import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.UUIDGrant;

import java.util.List;

/**
 * Legacy (App Context v3) grant world. Adds the {@code uuids(...)} bucket and the {@code authorizedUUID(...)}
 * principal on top of the shared {@link AbstractGrantTokenBuilder} setters. DataSync
 * {@code users(...)}/{@code dataSync(...)} buckets are intentionally absent here — they live on
 * {@link GrantTokenDataSyncBuilder}, reached from {@link GrantTokenBuilder} via {@code users(...)}/{@code dataSync(...)}.
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

    GrantTokenObjectsBuilder uuids(List<UUIDGrant> uuids);

    GrantTokenObjectsBuilder authorizedUUID(String authorizedUUID);
}