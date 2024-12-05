package com.pubnub.internal.endpoints.objects.internal

data class IncludeQueryParam(
    // common
    private val includeCustom: Boolean = false,
    private val includeType: Boolean = true,
    private val includeStatus: Boolean = true,
    // channel(membership) related
    private val includeChannel: Boolean = false,
    private val includeChannelCustom: Boolean = false,
    private val includeChannelStatus: Boolean = false,
    private val includeChannelType: Boolean = false,
    // user(member) related
    private val includeUser: Boolean = false,
    private val includeUserCustom: Boolean = false,
    private val includeUserStatus: Boolean = false,
    private val includeUserType: Boolean = false,
) {
    internal companion object {
        const val QUERY_PARAM_CUSTOM = "custom"
        const val QUERY_PARAM_TYPE = "type"
        const val QUERY_PARAM_STATUS = "status"
        const val QUERY_PARAM_CHANNEL = "channel"
        const val QUERY_PARAM_CHANNEL_CUSTOM = "channel.custom"
        const val QUERY_PARAM_CHANNEL_STATUS = "channel.status"
        const val QUERY_PARAM_CHANNEL_TYPE = "channel.type"
        const val QUERY_PARAM_UUID = "uuid"
        const val QUERY_PARAM_UUID_CUSTOM = "uuid.custom"
        const val QUERY_PARAM_UUID_STATUS = "uuid.status"
        const val QUERY_PARAM_UUID_TYPE = "uuid.type"
        const val QUERY_KEY_INCLUDE = "include"
    }

    internal fun createIncludeQueryParams(): Map<String, String> {
        val includeMapping: List<Pair<Boolean, String>> = listOf(
            includeCustom to QUERY_PARAM_CUSTOM,
            includeType to QUERY_PARAM_TYPE,
            includeStatus to QUERY_PARAM_STATUS,
            includeChannel to QUERY_PARAM_CHANNEL,
            includeChannelCustom to QUERY_PARAM_CHANNEL_CUSTOM,
            includeChannelStatus to QUERY_PARAM_CHANNEL_STATUS,
            includeChannelType to QUERY_PARAM_CHANNEL_TYPE,
            includeUser to QUERY_PARAM_UUID,
            includeUserCustom to QUERY_PARAM_UUID_CUSTOM,
            includeUserStatus to QUERY_PARAM_UUID_STATUS,
            includeUserType to QUERY_PARAM_UUID_TYPE
        )

        val includeList = includeMapping.filter { it.first }.map { it.second }

        return if (includeList.isNotEmpty()) {
            mapOf(QUERY_KEY_INCLUDE to includeList.joinToString(","))
        } else {
            mapOf()
        }
    }
}
