package com.pubnub.api.subscribe.internal

data class SubscriptionStatus(
    val channels: Collection<String> = setOf(),
    val groups: Collection<String> = setOf(),
    val cursor: Cursor? = null
)

data class Cursor(
    val timetoken: Long,
    val region: String
)