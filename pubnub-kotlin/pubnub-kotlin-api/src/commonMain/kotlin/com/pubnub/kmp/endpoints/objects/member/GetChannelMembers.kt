package com.pubnub.kmp.endpoints.objects.member

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult

/**
 * @see [PubNub.getChannelMembers]
 */
interface GetChannelMembers : PNFuture<PNMemberArrayResult>