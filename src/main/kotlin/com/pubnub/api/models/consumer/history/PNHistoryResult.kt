package com.pubnub.api.models.consumer.history

class PNHistoryResult internal constructor(
    val messages: List<PNHistoryItemResult>,
    val startTimetoken: Long,
    val endTimetoken: Long
)