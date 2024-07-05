package com.pubnub.kmp.endpoints.objects.membership

import ManageMembershipsResponse
import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.kmp.toChannelMetadata
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.kmp.toMap
import com.pubnub.kmp.toPNChannelMembershipArrayResult

class SetMembershipsImpl(pubnub: PubNub, params: PubNub.SetMembershipsParameters) : ManageMemberships,
    EndpointImpl<ManageMembershipsResponse, PNChannelMembershipArrayResult>(promiseFactory = { pubnub.objects.setMemberships(params) },
        responseMapping = ManageMembershipsResponse::toPNChannelMembershipArrayResult
    )
