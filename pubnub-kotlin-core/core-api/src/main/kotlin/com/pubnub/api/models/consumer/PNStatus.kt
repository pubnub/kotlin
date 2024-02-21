package com.pubnub.api.models.consumer

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNStatusCategory

data class PNStatus(
    val category: PNStatusCategory,
    val exception: PubNubException? = null,
    val currentTimetoken: Long? = null,
    val channels: Collection<String> = emptySet(),
    val channelGroups: Collection<String> = emptySet()
) {
    @get:JvmName("isError")
    val error: Boolean = exception != null
}
