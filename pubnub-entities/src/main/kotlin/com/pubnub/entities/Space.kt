package com.pubnub.entities

import com.pubnub.api.PubNub
import com.pubnub.entities.objects.endpoint.channel.GetSpace
import com.pubnub.entities.objects.endpoint.internal.ReturningCustom

fun PubNub.getSpace(
    space: String,
    includeCustom: Boolean = false
) = GetSpace(
    pubnub = this,
    channel = space,
    withCustom = ReturningCustom(includeCustom = includeCustom)
)

