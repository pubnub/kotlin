package com.pubnub.api.endpoints.objects.membership

import ManageMembershipsResponse
import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.kmp.toPNChannelMembershipArrayResult

class SetMembershipsImpl(pubnub: PubNub, params: PubNub.SetMembershipsParameters) : ManageMemberships,
    EndpointImpl<ManageMembershipsResponse, PNChannelMembershipArrayResult>(
        promiseFactory = { pubnub.objects.setMemberships(params) },
        responseMapping = ManageMembershipsResponse::toPNChannelMembershipArrayResult
    )
