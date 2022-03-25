package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.ExtendedState

data class SubscriptionStatus(
    val channels: Collection<String> = setOf(),
    val groups: Collection<String> = setOf(),
    val cursor: Cursor? = null
) : ExtendedState

data class Cursor(
    val timetoken: Long,
    val region: String
)