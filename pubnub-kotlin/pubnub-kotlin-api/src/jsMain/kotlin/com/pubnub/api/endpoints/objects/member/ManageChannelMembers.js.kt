package com.pubnub.api.endpoints.objects.member

import PubNub
import RemoveMessageActionResult
import com.pubnub.api.Endpoint
import com.pubnub.api.EndpointImpl
import com.pubnub.api.endpoints.message_actions.RemoveMessageAction
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult

/**
 * @see [PubNub.manageChannelMembers]
 */
actual interface ManageChannelMembers : Endpoint<PNMemberArrayResult>