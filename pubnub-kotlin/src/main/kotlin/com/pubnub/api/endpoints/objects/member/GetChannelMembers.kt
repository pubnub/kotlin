package com.pubnub.api.endpoints.objects.member

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.map
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.objects.member.GetChannelMembers
import com.pubnub.internal.endpoints.objects.member.IGetChannelMembers

/**
 * @see [PubNub.getChannelMembers]
 */
class GetChannelMembers internal constructor(private val getChannelMembers: GetChannelMembers) :
    DelegatingEndpoint<PNMemberArrayResult>(), IGetChannelMembers by getChannelMembers {

    override fun createAction(): Endpoint<PNMemberArrayResult> = getChannelMembers.map(PNMemberArrayResult::from)
}