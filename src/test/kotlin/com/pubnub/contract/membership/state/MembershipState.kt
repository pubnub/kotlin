package com.pubnub.contract.uuidmetadata.state

import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership

class MembershipState(
    private val uuidMetadataState: UUIDMetadataState
) {
    fun uuid(): String? = uuidMetadataState.uuid
    var membership: PNChannelMembership? = null
    var memberships: Collection<PNChannelMembership>? = null
}
