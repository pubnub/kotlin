package com.pubnub.internal.kotlin.endpoints.objects.member

import com.pubnub.api.DelegatingEndpoint
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.objects.member.IManageChannelMembers
import com.pubnub.internal.endpoints.objects.member.ManageChannelMembers
import com.pubnub.internal.models.consumer.objects.member.PNMemberArrayResult

/**
 * @see [PubNubImpl.manageChannelMembers]
 */
class ManageChannelMembersImpl internal constructor(manageChannelMembers: ManageChannelMembers) :
    DelegatingEndpoint<com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult, PNMemberArrayResult>(
        manageChannelMembers
    ),
    IManageChannelMembers by manageChannelMembers,
    com.pubnub.api.endpoints.objects.member.ManageChannelMembers {
    override fun convertAction(remoteAction: ExtendedRemoteAction<PNMemberArrayResult>): ExtendedRemoteAction<com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult> {
        return MappingRemoteAction(
            remoteAction,
            com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult::from
        )
    }
}
