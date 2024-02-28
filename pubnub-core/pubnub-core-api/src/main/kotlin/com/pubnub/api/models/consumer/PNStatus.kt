package com.pubnub.api.models.consumer

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNStatusCategory

class PNStatus(
    val category: PNStatusCategory,
    val exception: PubNubException? = null,
    val currentTimetoken: Long? = null,
    val channels: Collection<String> = emptySet(),
    val channelGroups: Collection<String> = emptySet(),
) {
    @get:JvmName("isError")
    val error: Boolean = exception != null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PNStatus) return false

        if (category != other.category) return false
        if (exception != other.exception) return false
        if (currentTimetoken != other.currentTimetoken) return false
        if (channels != other.channels) return false
        if (channelGroups != other.channelGroups) return false

        return true
    }

    override fun hashCode(): Int {
        var result = category.hashCode()
        result = 31 * result + (exception?.hashCode() ?: 0)
        result = 31 * result + (currentTimetoken?.hashCode() ?: 0)
        result = 31 * result + channels.hashCode()
        result = 31 * result + channelGroups.hashCode()
        return result
    }


}
