package com.pubnub.api.endpoints.objects.membership

import ManageMembershipsResponse
import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.kmp.toPNChannelMembershipArrayResult

class GetMembershipsImpl(pubnub: PubNub, params: PubNub.GetMembershipsParametersv2) : GetMemberships,
    EndpointImpl<ManageMembershipsResponse, PNChannelMembershipArrayResult>(promiseFactory = { pubnub.objects.getMemberships(params) },
        responseMapping = ManageMembershipsResponse::toPNChannelMembershipArrayResult
    )
