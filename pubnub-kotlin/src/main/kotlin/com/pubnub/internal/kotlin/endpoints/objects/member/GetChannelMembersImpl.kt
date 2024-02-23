package com.pubnub.internal.kotlin.endpoints.objects.member

import com.pubnub.api.DelegatingEndpoint
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.objects.member.GetChannelMembers
import com.pubnub.internal.endpoints.objects.member.IGetChannelMembers
import com.pubnub.internal.models.consumer.objects.member.PNMemberArrayResult

/**
 * @see [PubNubImpl.getChannelMembers]
 */
class GetChannelMembersImpl internal constructor(getChannelMembers: GetChannelMembers) :
    DelegatingEndpoint<com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult, PNMemberArrayResult>(
        getChannelMembers
    ),
    IGetChannelMembers by getChannelMembers,
    com.pubnub.api.endpoints.objects.member.GetChannelMembers {
    override fun convertAction(remoteAction: ExtendedRemoteAction<PNMemberArrayResult>): ExtendedRemoteAction<com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult> {
        return MappingRemoteAction(
            remoteAction,
            com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult::from
        )
    }
}
