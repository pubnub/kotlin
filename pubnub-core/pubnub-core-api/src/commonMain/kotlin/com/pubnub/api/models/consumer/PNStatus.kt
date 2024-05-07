package com.pubnub.api.models.consumer

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNStatusCategory
import kotlin.jvm.JvmName

class PNStatus(
    val category: PNStatusCategory,
    val exception: PubNubException? = null,
    val currentTimetoken: Long? = null,
    val affectedChannels: Collection<String> = emptySet(),
    val affectedChannelGroups: Collection<String> = emptySet(),
) {
    @get:JvmName("isError")
    val error: Boolean = exception != null

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is PNStatus) {
            return false
        }

        if (category != other.category) {
            return false
        }
        if (exception != other.exception) {
            return false
        }
        if (currentTimetoken != other.currentTimetoken) {
            return false
        }
        if (affectedChannels != other.affectedChannels) {
            return false
        }
        if (affectedChannelGroups != other.affectedChannelGroups) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = category.hashCode()
        result = 31 * result + (exception?.hashCode() ?: 0)
        result = 31 * result + (currentTimetoken?.hashCode() ?: 0)
        result = 31 * result + affectedChannels.hashCode()
        result = 31 * result + affectedChannelGroups.hashCode()
        return result
    }
}
