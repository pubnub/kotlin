package com.pubnub.api.endpoints.objects.member

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult

/**
 * @see [PubNub.getChannelMembers]
 */
interface GetChannelMembers : com.pubnub.kmp.endpoints.objects.member.GetChannelMembers, Endpoint<PNMemberArrayResult>
