package com.pubnub.api.endpoints.objects.internal

import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel

data class ReturningCustom(
    private val includeCustom: Boolean = false
) {

    internal fun createIncludeQueryParams(): Map<String, String> {
        return if (includeCustom) {
            mapOf("include" to "custom")
        } else {
            mapOf()
        }
    }
}

data class ReturningChannelDetailsCustom(
    private val includeCustom: Boolean = false,
    private val includeChannelDetails: PNChannelDetailsLevel? = null
) {

    private fun includeList(
        includeCustom: Boolean?,
        channelDetailsLevel: PNChannelDetailsLevel?
    ): List<String> {
        val includeList = mutableListOf<String>()
        if (includeCustom == true) includeList.add("custom")
        when (channelDetailsLevel) {
            PNChannelDetailsLevel.CHANNEL -> includeList.add("channel")
            PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM -> includeList.add("channel.custom")
        }
        return includeList.toList()
    }

    internal fun createIncludeQueryParams(): Map<String, String> {
        val includeList = includeList(includeCustom = includeCustom,
                channelDetailsLevel = includeChannelDetails)
        return if (includeList.isNotEmpty()) {
            mapOf("include" to includeList.joinToString(","))
        } else {
            mapOf()
        }
    }
}

data class ReturningUUIDDetailsCustom(
    private val includeCustom: Boolean = false,
    private val includeUUIDDetails: PNUUIDDetailsLevel? = null
) {

    private fun includeList(
        includeCustom: Boolean?,
        uuidDetailsLevel: PNUUIDDetailsLevel?
    ): List<String> {
        val includeList = mutableListOf<String>()
        if (includeCustom == true) includeList.add("custom")
        when (uuidDetailsLevel) {
            PNUUIDDetailsLevel.UUID -> includeList.add("uuid")
            PNUUIDDetailsLevel.UUID_WITH_CUSTOM -> includeList.add("uuid.custom")
        }
        return includeList.toList()
    }

    internal fun createIncludeQueryParams(): Map<String, String> {
        val includeList = includeList(includeCustom = includeCustom,
                uuidDetailsLevel = includeUUIDDetails)
        return if (includeList.isNotEmpty()) {
            mapOf("include" to includeList.joinToString(","))
        } else {
            mapOf()
        }
    }
}
