package com.pubnub.api.endpoints.objects.member

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult

/**
 * @see [PubNub.manageChannelMembers]
 */
actual interface ManageChannelMembers : Endpoint<PNMemberArrayResult>