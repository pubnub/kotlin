package com.pubnub.api.endpoints.objects.member

import ManageChannelMembersResponse
import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.kmp.toPNMemberArrayResult

class SetChannelMembersImpl(pubnub: PubNub, params: PubNub.SetChannelMembersParameters) : ManageChannelMembers, EndpointImpl<ManageChannelMembersResponse, PNMemberArrayResult>(
    promiseFactory = { pubnub.objects.setChannelMembers(params) },
    responseMapping = ManageChannelMembersResponse::toPNMemberArrayResult
)
