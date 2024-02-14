package com.pubnub.api.endpoints.objects.member

import com.pubnub.api.DelegatingEndpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.internal.endpoints.objects.member.GetChannelMembers
import com.pubnub.internal.endpoints.objects.member.IGetChannelMembers
import com.pubnub.internal.models.consumer.objects.member.PNMemberArrayResult

/**
 * @see [PubNub.getChannelMembers]
 */
class GetChannelMembers internal constructor(getChannelMembers: GetChannelMembers) :
    DelegatingEndpoint<com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult, PNMemberArrayResult>(
        getChannelMembers
    ),
    IGetChannelMembers by getChannelMembers {
    override fun convertAction(remoteAction: ExtendedRemoteAction<PNMemberArrayResult>): ExtendedRemoteAction<com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult> {
        return MappingRemoteAction(
            remoteAction,
            com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult::from
        )
    }
}
