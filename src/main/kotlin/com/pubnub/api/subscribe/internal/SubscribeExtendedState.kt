package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.ExtendedState

data class SubscribeExtendedState(
    val channels: Set<String> = setOf(),
    val groups: Set<String> = setOf(),
    val cursor: Cursor? = null,
    val attempt: Int = 0
) : ExtendedState

data class Cursor(
    val timetoken: Long,
    val region: String
)
