package com.pubnub.api.endpoints.objects.member

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult

/**
 * @see [PubNub.manageChannelMembers]
 */
interface ManageChannelMembers : com.pubnub.kmp.endpoints.objects.member.ManageChannelMembers, Endpoint<PNMemberArrayResult>
