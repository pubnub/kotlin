package com.pubnub.api.java.endpoints.access.builder;

import com.pubnub.api.UserId;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.UUIDGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.UserGrant;

import java.util.List;

/**
 * Entry point for a {@code grantToken(...)} call. Inherits the shared {@code ttl}/{@code meta}/{@code channels}/
 * {@code channelGroups} setters from {@link AbstractGrantTokenBuilder} (returning this neutral builder so a chain can
 * start with any of them), then branches into one of two mutually-exclusive grant worlds:
 *
 * <ul>
 *   <li>the legacy (App Context v3) world via {@link #uuids(List)} or {@link #authorizedUUID(String)}, which returns
 *       {@link GrantTokenObjectsBuilder};</li>
 *   <li>the DataSync world via {@link #authorizedUserId(UserId)}, {@link #users(List)} or
 *       {@link #dataSync(List)}, which returns {@link GrantTokenDataSyncBuilder}.</li>
 * </ul>
 *
 * The two worlds cannot be combined: once a bucket from one world is supplied, the returned builder only exposes that
 * world's buckets, so the legacy {@code uuids} bucket can never be mixed with {@code users}/{@code dataSync}
 * buckets.
 */
public interface GrantTokenBuilder extends AbstractGrantTokenBuilder {
    /**
     * @param ttl
     * @return instance of this builder
     * @deprecated Use {@link com.pubnub.api.java.PubNub#grantToken(int)} instead.
     */
    @Deprecated
    @Override
    GrantTokenBuilder ttl(Integer ttl);

    @Override
    GrantTokenBuilder meta(Object meta);

    @Override
    GrantTokenBuilder channels(List<ChannelGrant> channels);

    @Override
    GrantTokenBuilder channelGroups(List<ChannelGroupGrant> channelGroups);

    GrantTokenObjectsBuilder uuids(List<UUIDGrant> uuids);

    /**
     * Sets the authorized principal for a legacy (App Context v3) grant and enters the v3 grant world,
     * where {@code uuids(...)} grants can be supplied.
     */
    GrantTokenObjectsBuilder authorizedUUID(String authorizedUUID);

    /**
     * Sets the authorized principal for DataSync grant and enters DataSync world, where
     * {@code users(...)}/{@code dataSync(...)} grants can be supplied. Mirrors the legacy {@link #authorizedUUID(String)}
     * gate. The v4 buckets cannot be combined with the legacy {@code uuids(...)} bucket.
     */
    GrantTokenDataSyncBuilder authorizedUserId(UserId authorizedUserId);

    /**
     * Enters the DataSync grant world with a {@code users(...)} grant. The authorized principal is optional in
     * this world and can be set with {@link GrantTokenDataSyncBuilder#authorizedUserId(UserId)}. The v4 buckets cannot
     * be combined with the legacy {@code uuids(...)} bucket.
     */
    GrantTokenDataSyncBuilder users(List<UserGrant> users);

    /**
     * Enters the DataSync grant world with a {@code dataSync(...)} grant. The authorized principal is optional in
     * this world and can be set with {@link GrantTokenDataSyncBuilder#authorizedUserId(UserId)}. The v4 buckets cannot
     * be combined with the legacy {@code uuids(...)} bucket.
     */
    GrantTokenDataSyncBuilder dataSync(List<DataSyncGrant> dataSync);
}