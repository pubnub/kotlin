package com.pubnub.api.java.endpoints.access.builder;

import com.pubnub.api.UserId;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.TokenGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.UUIDGrant;

import java.util.List;

/**
 * Entry point for a {@code grantToken(...)} call. Inherits the shared {@code ttl}/{@code meta}/{@code channels}/
 * {@code channelGroups} setters from {@link AbstractGrantTokenBuilder} (returning this neutral builder so a chain can
 * start with any of them).
 *
 * <p>From here two grant paths are available and cannot be combined:
 *
 * <ul>
 *   <li>the modern flat-list path via {@link #authorizedUserId(UserId)} and {@link #grants(List)}, which stays on this
 *       neutral builder — each {@link TokenGrant} carries its own bucket. This path is additive with the shared
 *       {@code channels(...)}/{@code channelGroups(...)} setters (they all funnel into the same wire buckets);</li>
 *   <li>the legacy (App Context v2 UUID) path via {@link #uuids(List)} or {@link #authorizedUUID(String)}, which
 *       returns {@link GrantTokenObjectsBuilder}.</li>
 * </ul>
 *
 * The legacy {@code uuids} bucket cannot be mixed with {@code grants(...)}: {@link UUIDGrant} does not implement
 * {@link TokenGrant}, and combining the two is rejected at request time.
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
     * Sets the authorized principal for a legacy (App Context v2 UUID) grant and enters the legacy grant path,
     * where {@code uuids(...)} grants can be supplied.
     */
    GrantTokenObjectsBuilder authorizedUUID(String authorizedUUID);

    /**
     * Sets the authorized principal for the modern flat-list grant path. Stays on this neutral builder — the
     * authorized principal maps to the same wire field as {@link #authorizedUUID(String)}. Cannot be combined with the
     * legacy {@code uuids(...)} bucket.
     */
    GrantTokenBuilder authorizedUserId(UserId authorizedUserId);

    /**
     * Supplies the flat list of grants for the modern grant path. Each {@link TokenGrant} carries its own resource
     * bucket ({@link ChannelGrant}, {@link ChannelGroupGrant},
     * {@link com.pubnub.api.java.models.consumer.access_manager.v3.UserGrant} or
     * {@link com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant}). Additive with
     * {@code channels(...)}/{@code channelGroups(...)}; cannot be combined with the legacy {@code uuids(...)} bucket.
     */
    GrantTokenBuilder grants(List<TokenGrant> grants);
}