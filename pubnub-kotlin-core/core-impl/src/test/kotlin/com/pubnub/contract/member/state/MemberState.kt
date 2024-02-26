package com.pubnub.contract.member.state

import com.pubnub.contract.channelmetadata.state.ChannelMetadataState
import com.pubnub.internal.models.consumer.objects.member.PNMember

class MemberState(
    private val channelMetadataState: ChannelMetadataState,
) {
    fun channelId(): String = channelMetadataState.channelId

    val members: MutableCollection<PNMember> = mutableListOf()
    val membersToRemove: MutableCollection<PNMember> = mutableListOf()
    lateinit var returnedMembers: Collection<PNMember>
}
