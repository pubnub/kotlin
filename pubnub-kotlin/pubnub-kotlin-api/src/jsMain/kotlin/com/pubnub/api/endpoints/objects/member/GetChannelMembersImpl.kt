package com.pubnub.api.endpoints.objects.member

import ManageChannelMembersResponse
import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.kmp.toMap

class GetChannelMembersImpl(pubnub: PubNub, params: PubNub.GetChannelMembersParameters) : GetChannelMembers, EndpointImpl<ManageChannelMembersResponse, PNMemberArrayResult>(
    promiseFactory = { pubnub.objects.getChannelMembers(params) },
    responseMapping = {
        PNMemberArrayResult(
            it.status.toInt(),
            it.data.map { member ->
                PNMember(
                    PNUUIDMetadata(
                        member.uuid.id,
                        member.uuid.name,
                        member.uuid.externalId,
                        member.uuid.profileUrl,
                        member.uuid.email,
                        member.uuid.custom?.toMap(),
                        member.uuid.updated,
                        member.uuid.eTag,
                        member.uuid.type,
                        member.uuid.status
                    ),
                    member.custom?.toMap(),
                    member.updated,
                    member.eTag,
                    member.status
                )
            },
            it.totalCount?.toInt(),
            it.next?.let { next -> PNPage.PNNext(next) },
            it.prev?.let { prev -> PNPage.PNPrev(prev) },
        )
    }
)