package com.pubnub.contract.member.state

import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.contract.channelmetadata.state.ChannelMetadataState
import com.pubnub.contract.state.World
import com.pubnub.contract.state.WorldState

class MemberState(
    private val channelMetadataState: ChannelMetadataState,
    world: World
) : WorldState by world {
    fun channelId(): String = channelMetadataState.channelId
    val members: MutableCollection<PNMember> = mutableListOf()
    val membersToRemove: MutableCollection<PNMember> = mutableListOf()
    lateinit var returnedMembers: Collection<PNMember>
}
