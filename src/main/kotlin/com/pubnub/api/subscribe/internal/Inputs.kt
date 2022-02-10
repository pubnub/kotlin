package com.pubnub.api.subscribe.internal

import com.pubnub.api.subscribe.SInput

data class SubscribeInput(
    val channels: List<String>,
    val groups: List<String> = listOf()
) : SInput

data class UnsubscribeInput(
    val channels: List<String>,
    val groups: List<String> = listOf()
) : SInput


