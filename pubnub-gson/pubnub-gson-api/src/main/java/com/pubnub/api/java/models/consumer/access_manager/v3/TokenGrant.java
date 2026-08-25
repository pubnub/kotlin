package com.pubnub.api.java.models.consumer.access_manager.v3;

/**
 * Marker for the grant types accepted by the flat-list {@code grants(...)} setter of the neutral
 * {@link com.pubnub.api.java.endpoints.access.builder.GrantTokenBuilder}.
 *
 * <p>Implemented by {@link ChannelGrant}, {@link ChannelGroupGrant}, {@link UserGrant} and {@link DataSyncGrant}.
 * {@link UUIDGrant} deliberately does <em>not</em> implement it: the legacy {@code uuids} bucket is reachable only
 * through the legacy {@code authorizedUUID(...)}/{@code uuids(...)} path, so a caller building a flat grant list
 * cannot accidentally target it. The exclusion is enforced at compile time by this marker rather than at runtime.
 *
 * <p>This marker is placed on the concrete grant classes directly (not on {@link PNResource}/
 * {@link PNAppContextResource}), because {@link UUIDGrant} extends {@link PNAppContextResource} and must stay excluded.
 */
public interface TokenGrant {
}
