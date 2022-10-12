package com.pubnub.contract.member.state

import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.contract.channelmetadata.state.ChannelMetadataState

class MemberState(
    private val channelMetadataState: ChannelMetadataState
) {
    fun channelId(): String? = channelMetadataState.channelId
    var member: PNMember? = null
    var members: Collection<PNMember>? = null
}
