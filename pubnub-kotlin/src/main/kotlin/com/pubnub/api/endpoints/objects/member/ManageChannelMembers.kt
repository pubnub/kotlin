package com.pubnub.api.endpoints.objects.member

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.map
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.objects.member.IManageChannelMembers
import com.pubnub.internal.endpoints.objects.member.ManageChannelMembers

/**
 * @see [PubNub.manageChannelMembers]
 */
class ManageChannelMembers internal constructor(private val manageChannelMembers: ManageChannelMembers) :
    DelegatingEndpoint<PNMemberArrayResult>(), IManageChannelMembers by manageChannelMembers {

    override fun createAction(): Endpoint<PNMemberArrayResult> = manageChannelMembers.map(PNMemberArrayResult::from)
}