package com.pubnub.api.endpoints.objects.membership

import ManageMembershipsResponse
import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.endpoints.objects.channel.toChannelMetadata
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.kmp.toMap

class GetMembershipsImpl(pubnub: PubNub, params: PubNub.GetMembershipsParametersv2) : GetMemberships,
    EndpointImpl<ManageMembershipsResponse, PNChannelMembershipArrayResult>(promiseFactory = { pubnub.objects.getMemberships(params) },
        responseMapping = { response: ManageMembershipsResponse ->
            PNChannelMembershipArrayResult(
                response.status.toInt(),
                response.data.map {
                    PNChannelMembership(
                        it.channel?.toChannelMetadata(),
                        it.custom?.toMap(),
                        it.updated,
                        it.eTag,
                        it.status
                    )
                },
                response.totalCount?.toInt(),
                response.next?.let { PNPage.PNNext(it) },
                response.prev?.let { PNPage.PNPrev(it) }
            )
        }
    )
