package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.ExtendedState

data class SubscribeExtendedState(
    val channels: Collection<String> = setOf(),
    val groups: Collection<String> = setOf(),
    val cursor: Cursor? = null,
    val attempts: Int = 0
) : ExtendedState

data class Cursor(
    val timetoken: Long,
    val region: String
)