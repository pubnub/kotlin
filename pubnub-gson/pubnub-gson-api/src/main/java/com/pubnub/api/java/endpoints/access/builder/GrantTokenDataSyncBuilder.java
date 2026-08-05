package com.pubnub.api.java.endpoints.access.builder;

import com.pubnub.api.UserId;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.UserGrant;

import java.util.List;

/**
 * DataSync grant world. Adds the {@code users(...)} and {@code dataSync(...)} buckets and the optional
 * {@code authorizedUserId(...)} principal on top of the shared {@link AbstractGrantTokenBuilder} setters. Entered from
 * {@link GrantTokenBuilder} via {@code users(...)}/{@code dataSync(...)}, so the legacy
 * {@code uuids(...)}/{@code authorizedUUID(...)} bucket is intentionally absent here — the two grant worlds cannot be
 * combined.
 */
public interface GrantTokenDataSyncBuilder extends AbstractGrantTokenBuilder {

    @Override
    GrantTokenDataSyncBuilder ttl(Integer ttl);

    @Override
    GrantTokenDataSyncBuilder meta(Object meta);

    @Override
    GrantTokenDataSyncBuilder channels(List<ChannelGrant> channels);

    @Override
    GrantTokenDataSyncBuilder channelGroups(List<ChannelGroupGrant> channelGroups);

    GrantTokenDataSyncBuilder users(List<UserGrant> users);

    GrantTokenDataSyncBuilder dataSync(List<DataSyncGrant> dataSync);

    GrantTokenDataSyncBuilder authorizedUserId(UserId userId);
}
