package com.pubnub.api.models.consumer.access_manager.v3

/**
 * Marker for the grant types accepted by the flat-list overload of
 * [com.pubnub.api.PubNub.grantToken] (`grants: List<TokenGrant>`).
 *
 * Implemented by [ChannelGrant], [ChannelGroupGrant], [UserGrant] and [DataSyncGrantType]. [UUIDGrant] deliberately
 * does **not** implement it: the legacy `uuids` bucket is reachable only through the legacy `authorizedUUID`/`uuids`
 * overload, so new callers building a flat grant list cannot accidentally target it. The exclusion is enforced at
 * compile time by this marker rather than at runtime.
 */
interface TokenGrant : PNGrant
