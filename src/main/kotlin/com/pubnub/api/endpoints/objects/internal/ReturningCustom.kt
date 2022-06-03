package com.pubnub.api.endpoints.objects.internal

import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel

data class Include(
    private val includeCustom: Boolean = false,
    private val includeChannelDetails: PNChannelDetailsLevel? = null,
    private val includeUUIDDetails: PNUUIDDetailsLevel? = null,
    private val includeType: Boolean = false,
    private val includeStatus: Boolean = false
) {
    internal fun createIncludeQueryParams(): Map<String, String> {
        val includeList = mutableListOf<String>()
        if (includeCustom) includeList.add("custom")
        when (includeChannelDetails) {
            PNChannelDetailsLevel.CHANNEL -> includeList.add("channel")
            PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM -> includeList.add("channel.custom")
        }
        when (includeUUIDDetails) {
            PNUUIDDetailsLevel.UUID -> includeList.add("uuid")
            PNUUIDDetailsLevel.UUID_WITH_CUSTOM -> includeList.add("uuid.custom")
        }
        if (includeType) includeList.add("type")
        if (includeStatus) includeList.add("status")
        return if (includeList.isNotEmpty()) {
            mapOf("include" to includeList.joinToString(","))
        } else {
            mapOf()
        }
    }

}

@Deprecated("use Include", ReplaceWith("Include"))
data class ReturningCustom @Deprecated("use Include", ReplaceWith("Include(includeCustom = includeCustom)")) constructor(
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

@Deprecated("use Include", ReplaceWith("Include"))
data class ReturningChannelDetailsCustom @Deprecated("use Include", ReplaceWith("Include(includeCustom = includeCustom, includeChannelDetails = includeChannelDetails)"))constructor(
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
        val includeList = includeList(
            includeCustom = includeCustom,
            channelDetailsLevel = includeChannelDetails
        )
        return if (includeList.isNotEmpty()) {
            mapOf("include" to includeList.joinToString(","))
        } else {
            mapOf()
        }
    }
}

@Deprecated("use Include", ReplaceWith("Include"))
data class ReturningUUIDDetailsCustom @Deprecated("use Include", ReplaceWith("Include(includeCustom = includeCustom, includeUUIDDetails = includeUUIDDetails)")) constructor(
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
        val includeList = includeList(
            includeCustom = includeCustom,
            uuidDetailsLevel = includeUUIDDetails
        )
        return if (includeList.isNotEmpty()) {
            mapOf("include" to includeList.joinToString(","))
        } else {
            mapOf()
        }
    }
}
