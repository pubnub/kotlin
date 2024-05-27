package com.pubnub.api.endpoints.objects.member

import ManageChannelMembersResponse
import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.kmp.toMap
import com.pubnub.kmp.toPNMember
import com.pubnub.kmp.toPNMemberArrayResult

class SetChannelMembersImpl(pubnub: PubNub, params: PubNub.SetChannelMembersParameters) : ManageChannelMembers, EndpointImpl<ManageChannelMembersResponse, PNMemberArrayResult>(
    promiseFactory = { pubnub.objects.setChannelMembers(params) },
    responseMapping = ManageChannelMembersResponse::toPNMemberArrayResult
)