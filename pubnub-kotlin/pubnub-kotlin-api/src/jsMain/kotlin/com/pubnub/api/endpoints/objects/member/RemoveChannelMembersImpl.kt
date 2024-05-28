package com.pubnub.api.endpoints.objects.member

import ManageChannelMembersResponse
import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.kmp.toPNMemberArrayResult

class RemoveChannelMembersImpl(pubnub: PubNub, params: PubNub.RemoveChannelMembersParameters) : ManageChannelMembers, EndpointImpl<ManageChannelMembersResponse, PNMemberArrayResult>(
    promiseFactory = { pubnub.objects.removeChannelMembers(params) },
    responseMapping = ManageChannelMembersResponse::toPNMemberArrayResult
)

