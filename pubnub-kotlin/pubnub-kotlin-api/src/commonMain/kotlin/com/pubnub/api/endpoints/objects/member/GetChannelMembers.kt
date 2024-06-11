package com.pubnub.api.endpoints.objects.member

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult

/**
 * @see [PubNub.getChannelMembers]
 */
expect interface GetChannelMembers : PNFuture<PNMemberArrayResult>