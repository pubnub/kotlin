package com.pubnub.internal.endpoints.objects.member

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.models.consumer.objects.member.PNMemberArrayResult

/**
 * @see [PubNubImpl.manageChannelMembers]
 */
class ManageChannelMembersImpl internal constructor(manageChannelMembers: ManageChannelMembersEndpoint) :
    DelegatingEndpoint<com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult, PNMemberArrayResult>(
        manageChannelMembers,
    ),
    ManageChannelMembersInterface by manageChannelMembers,
    com.pubnub.api.endpoints.objects.member.ManageChannelMembers {
        override fun convertAction(
            remoteAction: ExtendedRemoteAction<PNMemberArrayResult>,
        ): ExtendedRemoteAction<com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult> {
            return MappingRemoteAction(
                remoteAction,
                com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult::from,
            )
        }
    }
