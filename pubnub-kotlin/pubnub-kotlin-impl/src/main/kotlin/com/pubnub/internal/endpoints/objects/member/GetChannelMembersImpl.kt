package com.pubnub.internal.endpoints.objects.member

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.internal.models.from

/**
 * @see [PubNubImpl.getChannelMembers]
 */
class GetChannelMembersImpl internal constructor(getChannelMembers: GetChannelMembersEndpoint) :
    DelegatingEndpoint<com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult, PNMemberArrayResult>(
        getChannelMembers,
    ),
    GetChannelMembersInterface by getChannelMembers,
    com.pubnub.api.endpoints.objects.member.GetChannelMembers {
        override fun convertAction(
            remoteAction: ExtendedRemoteAction<PNMemberArrayResult>,
        ): ExtendedRemoteAction<com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult> {
            return MappingRemoteAction(
                remoteAction,
                ::from,
            )
        }
    }
