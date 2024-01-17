package com.pubnub.api.endpoints.objects.member

import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.internal.endpoints.objects.member.IManageChannelMembers
import com.pubnub.internal.endpoints.objects.member.ManageChannelMembers
import com.pubnub.internal.models.consumer.objects.member.PNMemberArrayResult

/**
 * @see [PubNub.manageChannelMembers]
 */
class ManageChannelMembers internal constructor(manageChannelMembers: ManageChannelMembers) :
    DelegatingEndpoint<com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult, PNMemberArrayResult>(
        manageChannelMembers
    ), IManageChannelMembers by manageChannelMembers {
    override fun convertAction(remoteAction: ExtendedRemoteAction<PNMemberArrayResult>): ExtendedRemoteAction<com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult> {
        return MappingRemoteAction(
            remoteAction,
            com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult::from
        )
    }
}