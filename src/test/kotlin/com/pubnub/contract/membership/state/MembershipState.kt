package com.pubnub.contract.uuidmetadata.state

import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.contract.state.World
import com.pubnub.contract.state.WorldState

class MembershipState(
    private val uuidMetadataState: UUIDMetadataState,
    world: World
) : WorldState by world {
    fun uuid(): String? = uuidMetadataState.uuid
    lateinit var returnedMemberships: Collection<PNChannelMembership>
    val membershipsToRemove: MutableCollection<PNChannelMembership> = mutableListOf()
    val memberships: MutableCollection<PNChannelMembership> = mutableListOf()
}
