package com.pubnub.api.endpoints.objects.membership

import ManageMembershipsResponse
import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.kmp.toPNChannelMembershipArrayResult

class RemoveMembershipsImpl(pubnub: PubNub, params: PubNub.RemoveMembershipsParameters) : ManageMemberships,
    EndpointImpl<ManageMembershipsResponse, PNChannelMembershipArrayResult>(
        promiseFactory = { pubnub.objects.removeMemberships(params) },
        responseMapping = ManageMembershipsResponse::toPNChannelMembershipArrayResult
    )
