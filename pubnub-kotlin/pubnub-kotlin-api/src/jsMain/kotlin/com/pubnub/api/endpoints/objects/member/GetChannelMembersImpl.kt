package com.pubnub.api.endpoints.objects.member

import ManageChannelMembersResponse
import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.kmp.toPNMemberArrayResult

class GetChannelMembersImpl(pubnub: PubNub, params: PubNub.GetChannelMembersParameters) : GetChannelMembers, EndpointImpl<ManageChannelMembersResponse, PNMemberArrayResult>(
    promiseFactory = { pubnub.objects.getChannelMembers(params) },
    responseMapping = ManageChannelMembersResponse::toPNMemberArrayResult
)
