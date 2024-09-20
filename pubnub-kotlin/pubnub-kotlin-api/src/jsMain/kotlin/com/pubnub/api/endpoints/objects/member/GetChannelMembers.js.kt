package com.pubnub.api.endpoints.objects.member

import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.getChannelMembers]
 */
actual interface GetChannelMembers : PNFuture<PNMemberArrayResult>
